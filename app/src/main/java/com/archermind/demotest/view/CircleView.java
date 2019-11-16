package com.archermind.demotest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by bingye.tang on 2019/1/7.
 */
public class CircleView extends View {

    private int mWidth;
    private int mHeight;
    private int mCircleX = 60;
    private int mCircleY;
    private int mRadius;

    private Paint mPaint;
    private Paint mTextPaint;

    private int mCurValue;

    private MoveActionListener mMoveActionListener;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawCircle(canvas);
//        drawtext(canvas);
//        @SuppressLint("DrawAllocation") RectF mRect = new RectF(500, 0, 1000, 500);
//        onChildDraw(canvas, mRect);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawText("OUTER", 0, 400, mPaint);
        Paint paint3 = new Paint();
        paint3.setColor(Color.RED);
        paint3.setStyle(Paint.Style.FILL);
        paint3.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.OUTER));
        canvas.drawCircle(300, 400, 50, paint3);
        paint3.setColor(Color.WHITE);
        paint3.setMaskFilter(null);
        canvas.drawCircle(300, 400, 35, paint3);


    }

    private void onChildDraw(Canvas canvas, RectF mRectF) {

        RadialGradient radialGradient = new RadialGradient(
                mRectF.centerX(), mRectF.centerY(),
                Math.min(mRectF.centerX(), mRectF.centerY()),
                new int[]{Color.RED, 0x000000}, null,
                Shader.TileMode.CLAMP
        );


        mPaint.setShader(radialGradient);

        canvas.drawArc(mRectF, 0, 90, false, mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        Log.e("tby", "widthMeasureSpec = " + mWidth + " heightMeasureSpec=" + mHeight);
        mCircleY = mHeight / 2;
        mRadius = mHeight / 2;
        setMeasuredDimension(mWidth, mHeight);
        Log.e("tby", "mWidth = " + mWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();

                if (x < 60) {
                    x = 60;
                } else if (x > mWidth - 80) {
                    x = mWidth - 80;
                }
                mCircleX = x;
                mCurValue = (int) ((x - 60) * 100.0f / ((mWidth - 60 - 80)));
                if (mMoveActionListener != null) {
                    mMoveActionListener.moveChanged(x, mCurValue);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 画圆环
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#383f48"));
        canvas.drawCircle(mCircleX, mCircleY, mRadius, mPaint);
        mPaint.setColor(Color.parseColor("#1d2731"));
        canvas.drawCircle(mCircleX, mCircleY, mRadius - 2, mPaint);
    }

    /**
     * 画数字
     */
    private void drawtext(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        canvas.drawText(mCurValue + "%", mCircleX, mHeight / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2, mTextPaint);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

//        mTextPaint = new Paint();
//        mTextPaint.setColor(Color.WHITE);
//        mTextPaint.setAntiAlias(true);
//        mTextPaint.setTextSize(24);
//        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    public void setLength(int value) {
        mCurValue = value;
        mCircleX = (int) (value / 100.0f * (858 - 60 - 80) + 60);
        Log.e("tby", "mCircleX = " + mCircleX + " ,mWidth-60-80 = " + (mWidth - 60 - 80));
        invalidate();
    }

    public interface MoveActionListener {
        public void moveChanged(int x, int value);
    }
}
