package com.archermind.demotest;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {


    private static AnimationDrawable mAddressListBg;//通讯录
    private static AnimationDrawable mAddressSyn;//同步通讯录
    private static AnimationDrawable mComing;//打进
    private static AnimationDrawable mUp;//上升
    private static AnimationDrawable mRotate;//上升


    @BindView(R.id.show)
    ImageView show;
    @BindView(R.id.show1)
    Button show1;
    @BindView(R.id.show2)
    Button show2;
    @BindView(R.id.comingImage)
    ImageView mComingImage;
    @BindView(R.id.upImage)
    ImageView mUpImage;
    @BindView(R.id.rotateImage)
    ImageView mRotateImage;
    private boolean isRunning = false;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //通讯录列表
        initAnim();
        //
        initImageAnim();
        initUpAnim();
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
        rt.play();

    }

    private void initImageAnim() {

        mComingImage.setAlpha(0f);
    }

    private void initAnim() {
        mAddressListBg = (AnimationDrawable) getResources().getDrawable(R.drawable.addresslist_new, null);
        //拨打电话
        mAddressSyn = (AnimationDrawable) getResources().getDrawable(R.drawable.addresslist_syn_new, null);

        mComing = (AnimationDrawable) getResources().getDrawable(R.drawable.coming, null);

        mUp = (AnimationDrawable) getResources().getDrawable(R.drawable.point_up, null);

        mRotate = (AnimationDrawable) getResources().getDrawable(R.drawable.point_cycle, null);
    }

    private ObjectAnimator objectAnimator;

    private void initUpAnim() {
        objectAnimator = ObjectAnimator.ofFloat(mComingImage, "alpha", 1f, 0f, 1f);
        objectAnimator.setDuration(1000);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private String TAG = "--->";

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long valueTime = animation.getCurrentPlayTime();

                if (valueTime == 700) {
                    runAmin(mUp);
                    int duration = 0;
                    for (int i = 0; i < mUp.getNumberOfFrames(); i++) {
                        duration += mUp.getDuration(i);
                    }
                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {

                        public void run() {
                            runAmin(mRotate);
                        }

                    }, duration);
                }
            }
        });
    }

    @OnClick({R.id.show, R.id.show1, R.id.show2})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.show://拨打

                runAmin(mComing);
                break;
            case R.id.show1://接通

                if (!objectAnimator.isRunning()) {
                    objectAnimator.start();
                } else {
                    objectAnimator.resume();
                }
                break;
            case R.id.show2://挂断
                mComingImage.animate().alpha(0f).setDuration(1000);

                break;
        }
    }

    private void runAmin(AnimationDrawable mComing) {
        AnimationDrawable getDra = (AnimationDrawable) mComingImage.getBackground();
        if (mComingImage.getAlpha() == 0) {
            mComingImage.animate().alpha(1).setDuration(300);
        }
        if (getDra != mComing) {
            mComingImage.setBackground(mComing);
        }
        if (!mComing.isRunning()) {
            mComing.start();
        } else {
            mComing.run();
        }
    }
}




