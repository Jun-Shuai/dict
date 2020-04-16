package com.example.dict.util;

import android.media.MediaPlayer;
import android.util.Log;
import androidx.annotation.NonNull;


import com.example.dict.util.interfaces.IPlayEvent;

import java.io.IOException;
import java.util.Queue;

public class PlayerUtil implements IPlayEvent {
    private PlayerUtil() {}

    @Override
    public void onReset() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onStart() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onCompletion() {
        if (--mTimes > 0) {
            this.onStart();
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    private static final class Single { private static final PlayerUtil ins = new PlayerUtil();}
    public static PlayerUtil getInstance() { return Single.ins; }

    private MediaPlayer mediaPlayer = new MediaPlayer();

    public void playMusic(@NonNull String pathName) {
        playMusic(pathName, 1);
    }

    private int mTimes = 1;
    public void playMusic(@NonNull String pathName, int times) {
        this.onReset(); // 每次来新的播放设置，重设状态
        mTimes = times;
        singlePlay(pathName);
    }

    private synchronized void singlePlay(@NonNull String pathName) {

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(pathName);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(false); // 循环播放
            mediaPlayer.start();
            this.onStart();
            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d("TAG","播放完成");
                this.onCompletion();
            });
        } catch (IOException e) {
            this.onCompletion();
        }
    }
}

