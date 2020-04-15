package com.example.dict.view.fragment;

import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
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

import com.example.dict.R;
import com.example.dict.adapter.DictAdapter;
import com.example.dict.databinding.FragmentHomeBinding;
import com.example.dict.model.MainViewModel;
import com.example.dict.room.entity.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;


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

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mView.recyclerview.setAdapter(adapter);
        mView.recyclerview.setLayoutManager(linearLayoutManager);
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(mView.recyclerview);

        String text = "0/5566";
//        SpannableString span = new SpannableString(text);
//        MaskFilterSpan embossMaskFilterSpan =
//                new MaskFilterSpan(new EmbossMaskFilter(new float[]{10, 10, 10}, 0.5f, 1, 1));
//        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),0 , text.length(), SPAN_INCLUSIVE_INCLUSIVE);
//        span.setSpan(embossMaskFilterSpan,0 , text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        mView.textProgress.setText(text);
    }

    private void initEvent() {
        // 点击播放音频
        adapter.setOnItemClickListence((position) -> {
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
}
