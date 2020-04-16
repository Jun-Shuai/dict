package com.example.dict.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dict.R;
import com.example.dict.adapter.DictAdapter;
import com.example.dict.conf.AppPermission;
import com.example.dict.databinding.FragmentHomeBinding;
import com.example.dict.model.MainViewModel;
import com.example.dict.room.entity.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private FragmentHomeBinding mView;
    private DictAdapter adapter;
    private List<Word> words;
    private String sdCardPath;

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
        words = new ArrayList<>();
        adapter = new DictAdapter(getContext(), words);
        sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        MainViewModel mViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mViewModel.getWordListModel().observe(requireActivity(), (list) -> {
            this.words.clear();
            this.words.addAll(list);
            adapter.notifyDataSetChanged();
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
        mView.textLeft.setText(String.valueOf("45%"));
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
        adapter.setOnItemClickListence((position) -> {
            if (Build.VERSION.SDK_INT >= 23) { // android 6.0
                if (!requestPermission()) {
                    Toast.makeText(getContext(), "请允许存储权限！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            String fileName = words.get(position).getMusic();
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(sdCardPath + "+/Android/" + fileName);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {

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
