package com.example.dict.view.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.example.dict.databinding.FragmentSplashBinding;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {

    private FragmentSplashBinding mView;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = FragmentSplashBinding.inflate(inflater, container, false);
        return mView.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AlphaAnimation anim = new AlphaAnimation(1f, 0.5f);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(2000);
        mView.splashImage.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mView.splashImage.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                mView.splashImage.setVisibility(View.GONE);
                Log.d("TAG","动画结束！");
                NavDirections action = SplashFragmentDirections.actionFragmentSplashToFragmentHome();
                Bundle bundle = new Bundle();
                bundle.putBoolean("showNavBottom",true);
                NavHostFragment.findNavController(SplashFragment.this).navigate(action.getActionId(), bundle);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG","SplashFragment onDestroy");
    }
}
