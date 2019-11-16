package com.archermind.demotest.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.archermind.demotest.R;

public class CircleLoadingView extends View {
    private final int mRingColor;
    private final int mGlobuleColor;
    private final float mRingWidth;
    private final float mGlobuleRadius;
    private final float mCycleTime;
    private Paint mPaint;
    private float mRingRadius;
    private String TAG = "CircleLoadingView---->";
    private ValueAnimator animator;
    private float heightPercentage;
    private Float angle = -1f;

    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs,
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
        attrsArray.recycle();
        mPaint = new Paint();
        initAnim();
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        animator = ValueAnimator.ofFloat(0f, 360f);//起始位置在最低点
        animator.setDuration((long) 10000)
                .setRepeatCount(
                        ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                angle = (Float) animation.getAnimatedValue();
                Log.v(TAG, "angle:-----"+angle);
                postInvalidate();
            }
        });
//        animator.setInterpolator(new LinearInterpolator());// 匀速旋转
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
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mGlobuleColor);
        canvas.drawCircle(central, central, mRingRadius, mPaint);// 绘制圆环
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

        RectF rectF = new RectF(central - mRingRadius, central - mRingRadius, central + mRingRadius, central + mRingRadius);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF, angle, angle, false, mPaint);

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

}
