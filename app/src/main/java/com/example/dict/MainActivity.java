package com.example.dict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.Manifest;
import android.app.DirectAction;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.example.dict.adapter.DictAdapter;
import com.example.dict.databinding.ActivityMainBinding;
import com.example.dict.model.MainViewModel;
import com.example.dict.retrofit.json.DictJson;
import com.example.dict.retrofit.params.Dict;
import com.example.dict.retrofit.IRetrofit;
import com.example.dict.room.AppDataBase;
import com.example.dict.room.entity.Word;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
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

public class MainActivity extends AppCompatActivity {

    private AppDataBase mDataBase;
    private ActivityMainBinding mView;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = ActivityMainBinding.inflate(LayoutInflater.from(this));
        mViewModel = new ViewModelProvider(getViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        setContentView(mView.getRoot());

        initEvent();

        Logger.addLogAdapter(new AndroidLogAdapter());

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
            }
        }

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHost).getNavController();

        mDataBase = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "my-database.db")
                .build();


        // 界面跳转
//        NavDirections action = HomeFragmentDirections.actionHomeFragmentToMineFragment();
//        Navigation.findNavController(v).navigate(action);


        Single<List<Word>> single = mDataBase.wordDao().getAll();
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Word>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Word> list) {
                        mViewModel.getWordListModel().postValue(list);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void initEvent() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_add, R.id.nav_love).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mView.bottomNav, navController);
    }


    private void getDictFromNet() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.77:8080/") //设置网络请求的Url地址
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
            public void onResponse(Call<DictJson> call, Response<DictJson> response) {
                Log.d("TAG",response.body().getMsg());
                Logger.d(response.body().getData());
                List<DictJson.DataBean> words = response.body().getData();
                Word[] arr = new Word[words.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = new Word(words.get(i));
                }
                Completable completable = mDataBase.wordDao().insertAll(arr);
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG","插入失败！"+e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<DictJson> call, Throwable t) {
                Log.d("TAG",t.toString());
            }
        });
    }
}
