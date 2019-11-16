package com.archermind.demotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainMenuActivity extends AppCompatActivity {


    @BindView(R.id.animation_view)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.animation_view2)
    LottieAnimationView lottieAnimationView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);


        initLottie();

    }

    private boolean isFirst = true;

    private void initLottie() {
        LottieComposition bt_calling = LottieComposition.Factory.fromFileSync(this,"bt_calling.json");
        LottieComposition bt_loop = LottieComposition.Factory.fromFileSync(this,"bt_loop.json");

        lottieAnimationView.setComposition(bt_calling);
        lottieAnimationView2.setComposition(bt_loop);

//        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if (isFirst) {
//                    isFirst = false;
//                    lottieAnimationView.setAnimation("bt_loop.json");
//                    lottieAnimationView.loop(true);
//                    lottieAnimationView.playAnimation();
//
//                }
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });

    }


}
