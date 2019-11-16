package com.archermind.demotest.activity.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Danxx on 2016/4/18.
 */
public class FocusAnimUtils {

    public static void focusAnim(View view, float scaleTo, float translationStart, float translationEnd, int alphaAll[]) {
        ArrayList<Animator> animatorList = new ArrayList<>();
        //缩放
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", scaleTo);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", scaleTo);
        //平移
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(view, "translationX", translationStart, translationEnd);
        //渐变
        ObjectAnimator alpha = null;
        if (alphaAll != null) {
            alpha = new ObjectAnimator().ofFloat(view, "alpha", alphaAll[0], alphaAll[1]);
            animatorList.add(alpha);
        }
        animatorList.add(animatorX);
        animatorList.add(animatorY);
        animatorList.add(translationX);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.playTogether(animatorList);
        animatorSet.start();

    }

}