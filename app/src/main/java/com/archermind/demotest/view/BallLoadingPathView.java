package com.archermind.demotest.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by SuperD on 2017/2/18.
 * 小球加载的运动轨迹
 */

public class BallLoadingPathView extends View implements View.OnClickListener {
    private static final int POINT_DISTANCE = 50;
    private static final int ANIM_RUNNING_TIME = 1000;

    //小球相关的描述
    private int mPointCount = 6;
    private int mPointRadius = 20;

    //中心点
    private int centerX;
    private int centerY;
    //首尾点之前的长度(加上隐藏的那个点,实际上有7个点)
    private float mWidth;

    //起始点,结束点
    private float mPointStartX;
    private float mPointEndX;
    private float mPointY;

    //绘制路径
    private PathMeasure mPathMeasure;
    //储存在路径移动过程中的点
    private float[] mPos = new float[2];
    private Path mPath;
    private float mPathLength;
    //轨迹动画的比率
    private float mPathRatio;

    private Paint mPaint;

    private ValueAnimator mPathAnim;


    //小球运动的标签  True的时候mPointFlag--向下 False的时候mPointFlag--- 向上
    private int mPointFlag = 1;


    public BallLoadingPathView(Context context) {
        this(context, null);
    }

    public BallLoadingPathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallLoadingPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPathMeasure = new PathMeasure();
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(5);

        mPathAnim = ValueAnimator.ofFloat(0, 1);
        mPathAnim.setDuration(ANIM_RUNNING_TIME);
        mPathAnim.setRepeatCount(ValueAnimator.INFINITE);
        mPathAnim.setInterpolator(new DecelerateInterpolator());
        mPathAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPathRatio = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mPathAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                mPointFlag = -mPointFlag;
            }
        });
        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        mWidth = (mPointCount - 1) * mPointRadius * 2 + (mPointCount - 1) * POINT_DISTANCE;
        mPointStartX = centerX - mWidth / 2;
        mPointEndX = centerX + mWidth / 2;
        mPointY = centerY;
        mPath.moveTo(mPointStartX, mPointY);
        mPath.quadTo(centerX, centerY + mWidth / 2, mPointEndX, mPointY);
        mPathMeasure.setPath(mPath, false);
        mPathLength = mPathMeasure.getLength();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mPointCount; i++) {
            if (i == 0) {
                mPathMeasure.getPosTan(mPathRatio * mPathLength, mPos, null);
                if (mPointFlag == 1) {
                    canvas.drawCircle(mPos[0], mPos[1], mPointRadius, mPaint);
                } else {
                    canvas.drawCircle(mPos[0], getSymmetryPointY(mPos[1]), mPointRadius, mPaint);
                }
            } else {
                float startPoint = mPointStartX + (POINT_DISTANCE + 2 * mPointRadius) * i;
                canvas.drawCircle(
                        startPoint - (POINT_DISTANCE + 2 * mPointRadius) * mPathRatio,
                        mPointY,
                        mPointRadius, mPaint);
            }
        }
    }

    /**
     * 获得当前Y坐标关于屏幕中心Y轴的对称点
     *
     * @param pointY
     * @return
     */
    private float getSymmetryPointY(float pointY) {
        float symmetryPointY = 2 * centerY - pointY;
        return symmetryPointY;
    }

    @Override
    public void onClick(View view) {
        mPathAnim.start();
    }
}
