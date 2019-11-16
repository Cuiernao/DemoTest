package com.archermind.demotest.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import com.archermind.demotest.R;

import java.util.Random;

/**
 * 波浪线
 */
public class WaveView extends View {
    private final int mRingColor;
    private final int mGlobuleColor;
    private final float mRingWidth;
    private final float mGlobuleRadius;
    private final float mCycleTime;
    private Paint mPaint;
    private float mRingRadius;
    private double currentAngle = -1;
    private String TAG = "---->";
    private ValueAnimator mComingAnimator;
    private float heightPercentage;
    private Float angle = 0f;
    private Float mComingAngle = 0f;
    private double mComingcurrentAngle = -1;
    //控制开始状态
    private double mBeginAngle = 270;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs,
                    int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.AccelerateCircularView, defStyle, 0);
        mRingColor = attrsArray.getColor(
                R.styleable.AccelerateCircularView_ringColor, Color.BLUE);
        mGlobuleColor = attrsArray.getColor(
                R.styleable.AccelerateCircularView_globuleColor, Color.WHITE);
        mRingWidth = attrsArray.getDimension(
                R.styleable.AccelerateCircularView_ringWidth, TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                                getResources().getDisplayMetrics()));
        mGlobuleRadius = attrsArray.getDimension(
                R.styleable.AccelerateCircularView_globuleRadius, TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                                getResources().getDisplayMetrics()));
        mCycleTime = attrsArray.getFloat(
                R.styleable.AccelerateCircularView_cycleTime, 5000);
        heightPercentage = attrsArray.getFloat(R.styleable.AccelerateCircularView_heightPercentage, 0.17f);
        if (heightPercentage > 1) {
            heightPercentage = 1f;
        }
        attrsArray.recycle();
        mPaint = new Paint();

        initComingAnim();
    }


    /**
     * 处理打入动画
     */
    private void initComingAnim() {
        mComingAnimator = ValueAnimator.ofFloat(0f, -180f);//起始位置在最低点
        mComingAnimator.setDuration((long) mCycleTime)
                .setRepeatCount(
                        ValueAnimator.INFINITE);
        mComingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mComingAngle = (Float) animation.getAnimatedValue();
                mComingcurrentAngle = mComingAngle * Math.PI / 180;
                invalidate();
            }
        });
        mComingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());// 匀速旋转
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: " + System.currentTimeMillis());
        int mWidth, mHeight;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = 169;
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSize);
            }

        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = 169;
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mWidth, heightSize);
            }

        }

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initDraw(canvas);

    }

    private void initDraw(Canvas canvas) {
        int central = Math.min(getWidth(), getHeight()) / 2;

        mRingRadius = central - mGlobuleRadius;

        if (mGlobuleRadius < mRingWidth / 2) {// 小球嵌在环里
            mRingRadius = central - mRingWidth / 2;
        }
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mRingColor);

        // canvas.drawCircle(central, central, mRingRadius, mPaint);// 绘制圆环
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mGlobuleColor);

        drawGlobule(canvas, central);// 绘制小球
    }


    /**
     * 绘制小球,起始位置为圆环最低点
     * <p>
     * rad 是弧度， deg 是角度
     * <p>
     * deg=rad*180/PI
     *
     * @param canvas
     * @param central
     */
    private void drawGlobule(Canvas canvas, float central) {


        for (int i = 0; i < 36; i++) {
            double index = 10 * Math.sin(Math.PI * mComingAngle / 180) * i;
            double flag = index + mBeginAngle;
            if (mComingAngle < -90) {
                flag = 180 - flag;
            }
            float cx = central + (float) (mRingRadius * Math.cos(Math.PI * flag / 180));
            float cy = (float) (central + mRingRadius / 5.4 * Math.sin(Math.PI * flag / 180));
            canvas.drawCircle(cx, cy, mGlobuleRadius, mPaint);
        }

    }


    /**
     * 开始动画
     */
    public void startComingAnimator() {

        if (!mComingAnimator.isRunning()) {
            mComingAnimator.start();
        }
    }

    /**
     * 取消拨打动画
     */
    public void cancelComeAnimtor() {

        if (!mComingAnimator.isRunning()) {
            mComingAnimator.cancel();
        }
    }


}
