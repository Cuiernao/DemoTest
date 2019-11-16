package com.archermind.demotest.view;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.archermind.demotest.R;

public class AccelerateCircularView extends View {
    private final int mRingColor;
    private final int mGlobuleColor;
    private final float mRingWidth;
    private final float mGlobuleRadius;
    private final float mCycleTime;
    private Paint mPaint;
    private float mRingRadius;
    private double currentAngle = -1;
    private String TAG = "---->";
    private ValueAnimator animator;
    private float heightPercentage;

    public AccelerateCircularView(Context context) {
        this(context, null);
    }

    public AccelerateCircularView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccelerateCircularView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.AccelerateCircularView, defStyle, 0);
        mRingColor = attrsArray.getColor(
                R.styleable.AccelerateCircularView_ringColor, Color.GRAY);
        mGlobuleColor = attrsArray.getColor(
                R.styleable.AccelerateCircularView_globuleColor, Color.BLUE);
        mRingWidth = attrsArray.getDimension(
                R.styleable.AccelerateCircularView_ringWidth, TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                                getResources().getDisplayMetrics()));
        mGlobuleRadius = attrsArray.getDimension(
                R.styleable.AccelerateCircularView_globuleRadius, TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                                getResources().getDisplayMetrics()));
        mCycleTime = attrsArray.getFloat(
                R.styleable.AccelerateCircularView_cycleTime, 3000);
        heightPercentage = attrsArray.getFloat(R.styleable.AccelerateCircularView_heightPercentage, 0.17f);
        if (heightPercentage > 1) {
            heightPercentage = 1f;
        }
        attrsArray.recycle();
        mPaint = new Paint();
        initAnim();
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        animator = ValueAnimator.ofFloat(360f, 0f);//起始位置在最低点
        animator.setDuration((long) mCycleTime)
                .setRepeatCount(
                        ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float angle = (Float) animation.getAnimatedValue();
                currentAngle = angle * Math.PI / 180;
                invalidate();
            }
        });
        animator.setInterpolator(new LinearInterpolator());// 匀速旋转
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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

        int central = Math.min(getWidth(), getHeight()) / 2;

        mRingRadius = central - mGlobuleRadius;

        if (mGlobuleRadius < mRingWidth / 2) {// 小球嵌在环里
            mRingRadius = central - mRingWidth / 2;
        }
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mRingColor);

//        canvas.drawCircle(central, central, mRingRadius, mPaint);// 绘制圆环
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
            double flag = 10 * Math.PI / 180 * i;
            float cx = central + (float) (mRingRadius * Math.cos(currentAngle + flag));
            float cy = (float) (central + mRingRadius * heightPercentage * Math.sin(currentAngle + flag));
            //计算圆环中小圆的半径
            double litterRadius = mGlobuleRadius * Math.abs(Math.sin(currentAngle + flag));
            canvas.drawCircle(cx, cy, (float) litterRadius, mPaint);
            Log.e(TAG, "[falg]+" + flag + "[litterRadius]" + litterRadius + ": [cx]" + cx);
        }
    }

    /**
     * 停止动画
     */
    public void stopMotion() {
        if (animator != null) {
            animator.cancel();
        }
    }


    /**
     * 旋转小球
     */
    public void startCirMotion() {

        animator.start();
    }

    public void setRotate(int dx) {
        currentAngle = dx * Math.PI / 180;
        currentAngle = currentAngle / 4;
        invalidate();
        Log.e(TAG, "setRotate: dx" + dx + "[currentAngle]" + currentAngle);
    }
}
