package com.test.wllnon.sirenxinyi.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.test.wllnon.sirenxinyi.R;

/**
 * Created by Administrator on 2016/4/2.
 */
public class AnimationUtils {
    private Animation outAnimation;
    private Animation inAnimation;

    private static AnimationUtils instance;
    public static AnimationUtils getInstance(Context context) {
        if (instance == null) {
            instance = new AnimationUtils(context);
        }
        return instance;
    }

    private AnimationUtils(Context context) {
        outAnimation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.icon_anticipate_scale_out);
        inAnimation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.icon_anticipate_scale_in);
    }

    public void animateFAB(final FloatingActionButton view, final int resId, final String title) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setIcon(resId);
            view.setTitle(title);
        } else {
            view.postOnAnimationDelayed(new Runnable() {
                @Override
                public void run() {
                    view.startAnimation(outAnimation);
                    view.setVisibility(View.INVISIBLE);
                }
            }, 0);
            view.postOnAnimationDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setIcon(resId);
                    view.setTitle(title);
                    view.setVisibility(View.VISIBLE);
                    view.startAnimation(inAnimation);
                }
            }, 500);
        }
    }

    public void animateOut(final View view, int duration) {

        if (Build.VERSION.SDK_INT < 16) {
            view.setVisibility(View.GONE);
        } else {
            view.postOnAnimationDelayed(new Runnable() {
                @Override
                public void run() {
                    view.startAnimation(outAnimation);
                    view.setVisibility(View.INVISIBLE);
                }
            }, duration);
        }
    }

    public void animateIn(final View view, int duration) {

        if (Build.VERSION.SDK_INT < 16) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.postOnAnimationDelayed(new Runnable() {
                @Override
                public void run() {
                    view.startAnimation(inAnimation);
                    view.setVisibility(View.VISIBLE);
                }
            }, duration);
        }
    }
}
