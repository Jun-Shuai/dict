package com.example.dict.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dict.MyApp;
import com.example.dict.R;
import com.example.dict.adapter.DictAdapter;
import com.example.dict.databinding.FragmentHomeBinding;
import com.example.dict.model.MainViewModel;
import com.example.dict.retrofit.IRetrofit;
import com.example.dict.retrofit.json.DictJson;
import com.example.dict.retrofit.params.Dict;
import com.example.dict.room.AppDataBase;
import com.example.dict.room.entity.Word;
import com.example.dict.util.PlayerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private FragmentHomeBinding mView;
    private DictAdapter adapter;
    private List<Word> words;
    private String sdCardPath;
    private MainViewModel mViewModel;
    private AppDataBase mDataBase;

    public HomeFragment() {
        Log.d("TAG","create HomeFragment！");
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = FragmentHomeBinding.inflate(inflater, container ,false);
        return mView.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TAG","onActivityCreated");
        initData();
        initView();
        initEvent();
    }

    private void initData() {
        MyApp app = (MyApp) Objects.requireNonNull(getActivity()).getApplication();
        mDataBase = app.getDataBase();
        words = new ArrayList<>();
        adapter = new DictAdapter(getContext(), words);
        sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mViewModel.getWordListModel().observe(requireActivity(), (list) -> {
            this.words.clear();
            this.words.addAll(list);
            adapter.notifyDataSetChanged();
        });
        loadDataFromDB();
    }

    /** 从数据库加载word数据 */
    private void loadDataFromDB() {
        Single<List<Word>> single = mDataBase.wordDao().getAll();
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Word>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onSuccess(List<Word> list) {
                        mViewModel.getWordListModel().postValue(list);
                        mDataBase.close();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mDataBase.close();
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mView.recyclerview.setAdapter(adapter);
        mView.recyclerview.setLayoutManager(linearLayoutManager);
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(mView.recyclerview);

        int text_left = 333;
        int text_right = 5566;
        mView.textLeft.setText(("41%"));
//        mView.textRight.setText("");

        // 绘制进度圆环
        mView.ringProgress.setPercentage(40f);
        mView.ringProgress.setBgColor(getResources().getColor(R.color.colorPrimary));
        mView.ringProgress.setStartColor(getResources().getColor(R.color.colorPrimary));
        mView.ringProgress.setEndColor(getResources().getColor(R.color.colorOrigin));
        mView.ringProgress.setGradient(true);
    }

    private void initEvent() {
        // 点击播放音频
        adapter.setOnItemClickListence((position, tag) -> {
            if (Build.VERSION.SDK_INT >= 23) { // android 6.0
                if (!requestPermission()) {
                    Toast.makeText(getContext(), "请允许存储权限！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            String fileName = sdCardPath + "/Android/" + words.get(position).getMusic();
            PlayerUtil.getInstance().playMusic(fileName, DictAdapter.CLICK_TAG_REPET == tag ? 5 : 1);

        });

        // 下拉刷新
        mView.freshLayout.setOnRefreshListener(() -> {
            Log.d("TAG","下拉刷新,请求数据！");
            refreshDataByNet();
        });
    }

    @Override
    public void onClick(View v) {

    }

    private void refreshDataByNet() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.99:8080/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        IRetrofit request = retrofit.create(IRetrofit.class);
        Dict params = new Dict();
        params.setStart("1");
        params.setEnd("6000");
        Call<DictJson> call = request.getDict(params); // id in 1..100
        call.enqueue(new Callback<DictJson>() {
            @Override
            public void onResponse(@NonNull Call<DictJson> call, @NonNull Response<DictJson> response) {
                mView.freshLayout.setRefreshing(false);
                List<DictJson.DataBean> words = response.body().getData();
                Word[] arr = new Word[words.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = new Word(words.get(i));
                }

                AppDataBase dataBase = Room.databaseBuilder(Objects.requireNonNull(getContext()).getApplicationContext(),
                        AppDataBase.class,
                        "my-database.db")
                        .build(); // 数据库

                Completable completable = dataBase.wordDao().insertAll(arr);
                completable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d("TAG","插入！");
                            }

                            @Override
                            public void onComplete() {
                                Log.d("TAG","插入完成！");
                                dataBase.close();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("TAG","插入失败！"+e.getMessage());
                                dataBase.close();
                            }
                        });
            }

            @Override
            public void onFailure(Call<DictJson> call, Throwable t) {
                Log.d("TAG",t.toString());
                mView.freshLayout.setRefreshing(false);
            }
        });
    }

    /** 需要申请文件读取权限（读取存储卡音频文件）*/
    private static final int TAG_READ_EXTERNAL_STORAGE = 0;
    private boolean requestPermission() {
        int flag = ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (flag == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},TAG_READ_EXTERNAL_STORAGE);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (TAG_READ_EXTERNAL_STORAGE == requestCode) {
            // 授权通过
            Log.d("TAG","已获取到权限 -> READ_EXTER NAL_STORAGE");
        }
    }
}
