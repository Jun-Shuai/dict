package com.example.dict;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.dict.databinding.ActivityMainBinding;
import com.example.dict.model.MainViewModel;
import com.example.dict.retrofit.json.DictJson;
import com.example.dict.retrofit.params.Dict;
import com.example.dict.retrofit.IRetrofit;
import com.example.dict.room.AppDataBase;
import com.example.dict.room.entity.Word;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    private ActivityMainBinding mView;
    private MainViewModel mViewModel;
    private NavController navController;
    private AppDataBase mDataBase;

    public AppDataBase getDataBase() {
        return mDataBase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Window window = getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // 数据库
        mDataBase = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class,
                "my-database.db")
                .build();

        mView = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(mView.getRoot());
        mViewModel = new ViewModelProvider(getViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class); // viewModel
        initView();
        initEvent();
        ininData();
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
//            }
//        }



        // 界面跳转
//        NavDirections action = HomeFragmentDirections.actionHomeFragmentToMineFragment();
//        Navigation.findNavController(v).navigate(action);
    }

    /** 设置界面 */
    private void initView() {
        // 设置导航
        BottomNavigationView navView = findViewById(R.id.bottom_nav);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        // 设置标题
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_insert, R.id.nav_love).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration); // 设置标题
    }
    /** 事件 */
    private void initEvent() {
        // Fragment切换事件
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_home) { // 切换到首页
                mView.bottomNav.setVisibility(View.VISIBLE); // 显示底部栏目
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 切换非全屏
            }

            if (destination.getId() == R.id.nav_splash) {
                Log.d("TAG","打开启动页");
            }
        });
    }
    /** 初始化数据 */
    private void ininData() {
        // 创建测试数据
//        createTestData();
        // 加载数据
        // TODO 部分加载，按页加载，预加载

//        getDictFromNet();
    }



    private void createTestData() {
        Word[] arr = new Word[120];
        for (int i = 0; i < 120; i++) {
            Word word = new Word();
            word.setUid(i);
            word.setWord("Helloworld"+i);
            word.setPhonogram("hello"+i);
            word.setWord_tream("小学三年级");
            word.setMusic("null");
            arr[i] = word;
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


    private void getDictFromNet() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.98:8080/") //设置网络请求的Url地址
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

    private long backTimes = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - backTimes > 1300) {
                backTimes = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                return false;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
