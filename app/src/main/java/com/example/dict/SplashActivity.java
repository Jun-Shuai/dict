package com.example.dict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import com.example.dict.databinding.ActivityMainBinding;
import com.example.dict.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding view = ActivitySplashBinding.inflate(LayoutInflater.from(this));
        setContentView(view.getRoot());

        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation anim = new AlphaAnimation(1f, 0.2f);
        anim.setDuration(4000);

        anim.setFillAfter(true);
        animationSet.addAnimation(anim);

        view.splashImage.startAnimation(animationSet);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.splashImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
