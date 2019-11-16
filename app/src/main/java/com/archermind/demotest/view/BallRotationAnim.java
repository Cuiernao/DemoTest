package com.archermind.demotest.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.archermind.demotest.R;


/**
 * 两个小球旋转动画
 */
public class BallRotationAnim extends View {
    private String TAG = "--->Ball";
    //默认最大半径
    private final static int DEFUALT_MAX_RADIUS = 25;
    //默认最小半径
    private final static int DEFUALT_MIN_RADIUS = 15;
    //默认两个球心距离
    private final static int DEFAULT_DISTANCE = 35;
    //默认第一个球颜色
    private final static int DEFAULT_ONE_BALL_COLOR = Color.argb(255, 247, 48, 46);
    //默认第二个球颜色
    private final static int DEFAULT_TWO_BALL_COLOR = Color.argb(255, 30, 199, 247);
    //默认是动画执行时间
    private final static long DEFUALT_ANIMATOR_DURATION = 1000;

    //当前画笔
    private Paint mPaint;
    //当前球最大半径
    private float mMaxRadius = DEFUALT_MAX_RADIUS;
    //当前球最小半径
    private float mMinRadius = DEFUALT_MIN_RADIUS;
    //两球心间隔距离
    private float mDistance = DEFAULT_DISTANCE;
    //动画时间
    private long mDuration = DEFUALT_ANIMATOR_DURATION;
    //第一个球颜色
    private int mOneColor = DEFAULT_ONE_BALL_COLOR;
    //第二个球颜色
    private int mTwoColor = DEFAULT_TWO_BALL_COLOR;

    private Ball mOneBall;
    private Ball mTwoBall;
    private int mCenterX;
    private int mCenterY;

    private AnimatorSet animatorSet;

    public BallRotationAnim(Context context) {
        this(context, null);
    }

    public BallRotationAnim(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallRotationAnim(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BallRotationAnim);
        mMaxRadius = typedArray.getDimension(R.styleable.BallRotationAnim_max_radius, DEFUALT_MAX_RADIUS);
        mMinRadius = typedArray.getDimension(R.styleable.BallRotationAnim_min_radius, DEFUALT_MIN_RADIUS);
        mOneColor = typedArray.getColor(R.styleable.BallRotationAnim_one_color, DEFAULT_ONE_BALL_COLOR);
        mTwoColor = typedArray.getColor(R.styleable.BallRotationAnim_two_color, DEFAULT_TWO_BALL_COLOR);
        mDistance = typedArray.getDimension(R.styleable.BallRotationAnim_distance, DEFAULT_DISTANCE);
        mDuration = typedArray.getInt(R.styleable.BallRotationAnim_duration, (int) DEFUALT_ANIMATOR_DURATION);
        typedArray.recycle();

        mOneBall = new Ball();
        mTwoBall = new Ball();
        mOneBall.color = mOneColor;
        mTwoBall.color = mTwoColor;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        initAnimator();
    }

    private void initAnimator() {
        //球移动到中间的半径
        float centerRadius = (mMaxRadius + mMinRadius) * 0.5f;

        //第一个球缩放动画
        ObjectAnimator oneScaleAnimator = ObjectAnimator.ofFloat(mOneBall, "radius", centerRadius, mMaxRadius, centerRadius, mMinRadius, centerRadius);
        //无线循环
        oneScaleAnimator.setRepeatCount(ValueAnimator.INFINITE);

        //第一个球移动动画
        ValueAnimator oneTranslationAnimtor = ValueAnimator.ofFloat(-1, 0, 1, 0, -1);
        oneTranslationAnimtor.setRepeatCount(ValueAnimator.INFINITE);
        oneTranslationAnimtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float x = mCenterX + mDistance * value;

                mOneBall.centerX = x;

                invalidate();
            }
        });


        //第二个球缩放动画
        ObjectAnimator twoScaleAnimator = ObjectAnimator.ofFloat(mTwoBall, "radius", centerRadius, mMinRadius, centerRadius, mMaxRadius, centerRadius);
        twoScaleAnimator.setRepeatCount(ValueAnimator.INFINITE);

        //第二个球移动动画
        ValueAnimator twoTranslationAnimtor = ValueAnimator.ofFloat(0, 1, 0, -1, 0);
        twoTranslationAnimtor.setRepeatCount(ValueAnimator.INFINITE);
        twoTranslationAnimtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                float x = mCenterX + mDistance * value;
                float y = mCenterY + mDistance * value;
                mTwoBall.centerX = x;
                mTwoBall.centerY = y;
                //第一个球
                float y2 = mCenterY + mDistance* value;
                mOneBall.centerY = y2;
            }
        });

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(oneScaleAnimator, oneTranslationAnimtor, twoScaleAnimator, twoTranslationAnimtor);
        animatorSet.setDuration(mDuration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量
        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //测量
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //先画半径小的球，产生旋转效果
        if (mOneBall.radius > mTwoBall.radius) {
            mPaint.setColor(mTwoBall.color);
            canvas.drawCircle(mTwoBall.centerX, mTwoBall.centerY, mTwoBall.radius, mPaint);
            mPaint.setColor(mOneBall.color);
            canvas.drawCircle(mOneBall.centerX, mOneBall.centerY, mOneBall.radius, mPaint);
        } else {
            mPaint.setColor(mOneBall.color);
            canvas.drawCircle(mOneBall.centerX, mOneBall.centerY, mOneBall.radius, mPaint);
            mPaint.setColor(mTwoBall.color);
            canvas.drawCircle(mTwoBall.centerX, mTwoBall.centerY, mTwoBall.radius, mPaint);
        }
        canvas.drawLine(0, mCenterY, getWidth(), mCenterY, mPaint);
        Log.d(TAG, "onDraw: mOneBall\n x :" + mOneBall.centerX + " y: " + mOneBall.centerY + " radius:" + mOneBall.radius);
        Log.e(TAG, "onDraw: mTwoBall\n x :" + mTwoBall.centerX + " y: " + mTwoBall.centerY + " radius:" + mTwoBall.radius);
    }

    /**
     * 监听view的显示隐藏
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimator();
        } else {
            startAnimator();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimator();
    }

    /**
     * 开始动画
     */
    private void startAnimator() {
        if (getVisibility() != VISIBLE || animatorSet == null || animatorSet.isRunning())
            return;
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimator();
    }

    /**
     * 停止动画
     */
    private void stopAnimator() {
        if (animatorSet == null)
            return;
        animatorSet.end();
    }

    /**
     * 球实体类
     */
    public class Ball {
        public float radius;//半径
        public float centerX;//圆心,x轴坐标
        public float centerY;//圆心,x轴坐标
        public int color;//颜色

        public float getCenterY() {
            return centerY;
        }

        public void setCenterY(float centerY) {
            this.centerY = centerY;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public float getCenterX() {
            return centerX;
        }

        public void setCenterX(float centerX) {
            this.centerX = centerX;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
