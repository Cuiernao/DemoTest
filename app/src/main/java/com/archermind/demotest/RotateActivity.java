package com.archermind.demotest;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.archermind.demotest.adpter.PhoneAdapter;
import com.archermind.demotest.view.AccelerateCircularView;
import com.archermind.demotest.view.CircleLoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RotateActivity extends AppCompatActivity {

    @BindView(R.id.circularView)
    CircleLoadingView circularView;
    @BindView(R.id.mRecyclView)
    RecyclerView mRecyclView;
    @BindView(R.id.ShowImage)
    ImageView mShowImage;
    private double currentAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate);
        ButterKnife.bind(this);
        circularView.startCirMotion();
        iniRW();
        initListener();

    }

    private void initListener() {

        ValueAnimator animator = ValueAnimator.ofFloat(360f, 0f);//起始位置在最低点
        animator.setDuration(5000)
                .setRepeatCount(
                        ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float angle = (Float) animation.getAnimatedValue();
                mShowImage.animate().rotation(angle).setDuration(0);
            }
        });
        animator.setInterpolator(new LinearInterpolator());// 匀速旋转
        animator.start();
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return resizedBitmap;
    }

    private int offSetX = 0;

    private void iniRW() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i + "");
        }

        PhoneAdapter phoneAdapter = new PhoneAdapter(R.layout.item, list);
        mRecyclView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mRecyclView.setAdapter(phoneAdapter);
        mRecyclView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("cui", "[onScrolled]: " + dx);
                offSetX += dx;

            }
        });
    }
}
