package com.cuiernao.rouletteview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 3D轮盘
 */
public class RouletteView extends View {

    private final static String TAG = "SVBt-CircleView";

    private final float reflectionDiance;
    private final int mGlobuleColor;
    private final float mRingWidth;
    private final float mGlobuleRadius;
    private final float mCycleTime;
    private final int mDensity;
    private final float proportionX;
    private final float proportionY;
    private boolean isTouch;
    private Paint mPaint;
    /**
     * 点运动轨迹的圆半径
     */
    private float mRingRadius;
    /**
     * 当前弧度
     */
    private double currentRadian = -1;

    public RouletteView(Context context) {
        this(context, null);
    }

    public RouletteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouletteView(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.RouletteView, defStyle, 0);
        reflectionDiance = attrsArray.getDimension(
                R.styleable.RouletteView_reflectionDiance, TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                                getResources().getDisplayMetrics()));
        mGlobuleColor = attrsArray.getColor(
                R.styleable.RouletteView_globuleColor, context.getResources().getColor(R.color.colorAccent));
        mRingWidth = attrsArray.getDimension(
                R.styleable.RouletteView_ringWidth, TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                                getResources().getDisplayMetrics()));
        mGlobuleRadius = attrsArray.getDimension(
                R.styleable.RouletteView_globuleRadius, TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                                getResources().getDisplayMetrics()));
        mCycleTime = attrsArray.getFloat(
                R.styleable.RouletteView_cycleTime, 3000);
        mDensity = attrsArray.getInteger(R.styleable.RouletteView_density, 60);
        proportionX = attrsArray.getFloat(R.styleable.RouletteView_proportionX, 1);
        proportionY = attrsArray.getFloat(R.styleable.RouletteView_proportionY, 0.17f);
        isTouch=attrsArray.getBoolean(R.styleable.RouletteView_isTouch,true);
        attrsArray.recycle();
        mPaint = new Paint();
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
            mWidth = 250;
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSize);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = 250;
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
        //canvas.drawCircle(central, central, mRingRadius, mPaint);// 绘制圆环
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mGlobuleColor);

        drawGlobule(canvas, central);// 绘制小球

    }


    /**
     * 绘制小球,起始位置为圆环最低点
     *
     * @param canvas
     * @param central
     */

    private void drawGlobule(Canvas canvas, float central) {
        /**
         * rad 是弧度， angle 是角度
         *
         * 弧度rad = n° * π / 180
         *
         * 周长C = 2π*r
         * 弧长C = ((2π*r)/360°) * n° = n° * (π*r/180°)
         *
         * 1、首先确定点数：36个点
         * 2、将这些点按照每隔10°的画在圆环上。
         * 3、从固定的一个位置开始绘制点
         * 4、这个点的位置会根据列表的位移量计算出一个弧度，根据这个弧度绘画出第一个点的位置，然后后续的点按照每10°的偏移量进行绘制
         */
        for (int i = 0; i < mDensity; i++) {
            double offsetRadian = 0; //offsetRadian为弧度偏移量
            //计算旋转的偏移量
            float offSetDensity = 360 / mDensity;
            if (offSetDensity < 1) {
                offSetDensity = 1;
            }
            offsetRadian = (offSetDensity * i) * Math.PI / 180;
            //计算某个点的x，y坐标
            float cx = central + (float) (mRingRadius * proportionX * Math.cos(currentRadian + offsetRadian));
            //压缩y轴的比例
            float cy = (float) (central + mRingRadius * proportionY * Math.sin(currentRadian + offsetRadian));
            double litterRadius = mGlobuleRadius * Math.abs(Math.sin(currentRadian + offsetRadian));
            float diance= (float) (reflectionDiance*(Math.abs(Math.sin(currentRadian + offsetRadian))));
            if (Math.sin(currentRadian + offsetRadian) < 0) {//上部分正常绘制
                mPaint.setColor(getContext().getColor(R.color.whiteColor_30));
                canvas.drawCircle(cx, cy, (float) litterRadius/2, mPaint);
                //绘制上部阴影
                mPaint.setColor(getContext().getColor(R.color.whiteColor_5));
                canvas.drawCircle(cx, cy + reflectionDiance, (float) litterRadius/2, mPaint);
            } else {
                if (i % 2 != 0) {//去掉下部范围内一半点
                    mPaint.setColor(getContext().getColor(R.color.white));
                    canvas.drawCircle(cx, cy, (float) litterRadius, mPaint);
                    //绘制底部阴影
                    mPaint.setColor(getContext().getColor(R.color.whiteColor_15));
                    canvas.drawCircle(cx, cy + reflectionDiance, (float) litterRadius, mPaint);
                }

            }

        }
    }

    /**
     * 旋转小球
     */
    private void startCirMotion() {
        ValueAnimator animator = ValueAnimator.ofFloat(360f, 0f);//起始位置在最低点
        animator.setDuration((long) mCycleTime).setRepeatCount(
                ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float angle = (Float) animation.getAnimatedValue();
                currentRadian = angle * Math.PI / 180;
                invalidate();
            }
        });
        animator.setInterpolator(new LinearInterpolator());// 匀速旋转
        animator.start();
    }

    float offSetX = 0;
    float lastX = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float currX = event.getX();
                offSetX += currX - lastX;
                currentRadian = offSetX * Math.PI / 180;
                lastX = currX;
                invalidate();

                break;
            case MotionEvent.ACTION_UP:
                offSetX = 0;
                break;
        }
        return isTouch;
    }

    public void setRotate(int dx) {
        currentRadian = (dx * Math.PI / 180) / 20;
        invalidate();
    }
}
