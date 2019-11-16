package com.archermind.demotest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.archermind.demotest.R;

/**
 * 进度条显示
 */
public class LoadingView extends View {
    private final int mBgColor;//背景色

    private final int mBeginColor;//背景色
    private final int mEndColor;//显示颜色
    private final int mDefaultRate;//当前比率
    private final int mDefaultMaxNum;//显示最大值
    private final int mMinNum;//显示最小值
    private Paint mPaint;
    private float mRadius;
    private int mRectHegiht = 40;
    private int mMoveRate = -1;
    private float offMarginTop = 20;
    private int myMaxNum;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.LoadingView, defStyleAttr, 0);
        mBgColor = attrsArray.getColor(R.styleable.LoadingView_bg_color, Color.GRAY);

        mBeginColor = attrsArray.getColor(R.styleable.LoadingView_begin_color, getContext().getResources().getColor(R.color.loadViewBegin_color));
        mEndColor = attrsArray.getColor(R.styleable.LoadingView_end_color, getContext().getResources().getColor(R.color.loadViewEnd_color));
        mRadius = attrsArray.getDimension(R.styleable.LoadingView_radius, 20);
        mDefaultRate = attrsArray.getInt(R.styleable.LoadingView_current_rate, 50);
        mMinNum = attrsArray.getInt(R.styleable.LoadingView_min_num, 0);
        mDefaultMaxNum = attrsArray.getInt(R.styleable.LoadingView_max_num, 1000);
        attrsArray.recycle();
        mPaint = new Paint();
        myMaxNum = mDefaultMaxNum;
        mMoveRate = mDefaultRate;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth((float) 5 );
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setShader(null);
        mPaint.setColor(mBgColor);
        int x = getWidth();
        int y = getHeight();
        int leftX = x / 4;
        int rightX = x * 3 / 4;
        int topY = (y - mRectHegiht) / 2;
        int botoomY = (y + mRectHegiht) / 2;
        RectF BgRect = new RectF(leftX, topY, rightX, botoomY);
        canvas.drawRoundRect(BgRect, mRadius, mRadius, mPaint);// 绘制背景

//
        mPaint.setStyle(Paint.Style.FILL);
        int offX = mMoveRate * (x / 2) / myMaxNum;
        int rightX_ = leftX + offX;
        RectF showRect = new RectF(leftX, topY, rightX_, botoomY);
        int colors[] = {mBeginColor, mEndColor};
        LinearGradient shader = new LinearGradient(leftX, 0, rightX_, 0, colors, null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(shader);

        canvas.drawRoundRect(showRect, mRadius, mRadius, mPaint);// 绘制上层背景
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(40);
        String str = mMoveRate + "/" + myMaxNum;
        mPaint.setShader(null);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(str, getWidth() / 2, BgRect.top - offMarginTop, mPaint);
    }

    /**
     * 设置最大值
     *
     * @param maxNum
     */
    public void setmMaxNum(int maxNum) {
        this.myMaxNum = maxNum;
        invalidate();
    }

    /**
     * 设置当前值
     *
     * @param mCurrentRate
     */
    public void setCurrentRate(int mCurrentRate) {
        if (mCurrentRate <= myMaxNum) {
            this.mMoveRate = mCurrentRate;
        } else {
            this.mMoveRate = myMaxNum;
        }
        invalidate();
    }
}
