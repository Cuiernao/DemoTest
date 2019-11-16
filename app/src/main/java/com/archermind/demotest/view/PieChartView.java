package com.archermind.demotest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.archermind.demotest.R;

/**
 * Created by bingye.tang on 2019/1/7.
 */
public class PieChartView extends View {
//    private Bitmap mInnerCircle;

    private int mHeight;
    private int mWidth;

    private int mInnerRadius;
    private int mArcWidth = 37;

    private int mArcLeft;
    private int mArcTop;
    private int mArcRight;
    private int mArcBottom;

    public PieChartView(Context context) {
        super(context);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();

//        mInnerCircle = BitmapFactory.decodeResource(getResources(), R.drawable.circle_0, null);
//        mInnerRadius = mInnerCircle.getWidth() / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mArcLeft = mWidth / 2 - mInnerRadius - mArcWidth / 2 - 8;
        mArcTop = mHeight / 2 - mInnerRadius - mArcWidth / 2 - 8;
        mArcRight = mWidth / 2 + mInnerRadius + mArcWidth / 2 + 8;
        mArcBottom = mHeight / 2 + mInnerRadius + mArcWidth / 2 + 8;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
//        drawInnerCircle(canvas);
//        drawPieView(canvas);
        drawOuter(canvas);
    }

    private Paint mArcViewPaint;
    private Paint mLinePaint;
    private Paint mShaderPaint;


    private void initPaint() {
        mArcViewPaint = new Paint();
        mArcViewPaint.setStrokeWidth(mArcWidth);
        mArcViewPaint.setStyle(Paint.Style.STROKE);
        mArcViewPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);

        mShaderPaint = new Paint();
        mShaderPaint.setStrokeWidth(200);
        mShaderPaint.setStyle(Paint.Style.STROKE);
        mShaderPaint.setAntiAlias(true);
//        mShaderPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.OUTER));
    }

    private int[] mAvcColors = {Color.RED, 0x8D079CEC, Color.TRANSPARENT};//进度条颜色（渐变色的2个点）

    /**
     * 绘制中心圆
     *
     * @param canvas
     */
//    private void drawInnerCircle(Canvas canvas) {
//        canvas.drawBitmap(mInnerCircle, mWidth / 2 - mInnerRadius, mHeight / 2 - mInnerRadius, null);
//    }
    private void drawPieView(Canvas canvas) {
        RectF oval = new RectF(mArcLeft, mArcTop, mArcRight, mArcBottom);
        SweepGradient sweepGradient = new SweepGradient(mWidth / 2, mHeight / 2, 0xcc264a99, 0xcc3660d2);

        mArcViewPaint.setShader(sweepGradient);
        canvas.drawArc(oval, 270, 45 - 2, false, mArcViewPaint);

        RectF oval2 = new RectF(mArcLeft - mArcWidth / 2, mArcTop - mArcWidth / 2, mArcRight + mArcWidth / 2, mArcBottom + mArcWidth / 2);
        mLinePaint.setShader(sweepGradient);
        canvas.drawArc(oval2, 270, 45 - 2, false, mLinePaint);

        SweepGradient sweepGradient2 = new SweepGradient(mWidth / 2, mHeight / 2, 0xbf90732c, 0xbfc5ad46);
        mArcViewPaint.setShader(sweepGradient2);
        canvas.drawArc(oval, 270 + 45, 25 - 2, false, mArcViewPaint);

        mLinePaint.setShader(sweepGradient2);
        canvas.drawArc(oval2, 270 + 45, 25 - 2, false, mLinePaint);

        SweepGradient sweepGradient3 = new SweepGradient(mWidth / 2, mHeight / 2, 0xb30f4d5e, 0xb31e90a7);
        mArcViewPaint.setShader(sweepGradient3);
        canvas.drawArc(oval, 270 + 45 + 25, 360 - 45 - 25 - 2, false, mArcViewPaint);

        mLinePaint.setShader(sweepGradient3);
        canvas.drawArc(oval2, 270 + 45 + 25, 360 - 45 - 25 - 2, false, mLinePaint);

    }

    private void drawOuter(Canvas canvas) {


        RectF mRectF = new RectF(175, 0, 625, 450);
        RadialGradient radialGradient = new RadialGradient(
                mRectF.centerX(), mRectF.centerY(),
                Math.min(mRectF.centerX(), mRectF.centerY()),
                new int[]{0x66079CEC, Color.TRANSPARENT}, null,
                Shader.TileMode.CLAMP
        );
        mShaderPaint.setAntiAlias(true);

        mShaderPaint.setFilterBitmap(true);
        mShaderPaint.setShader(radialGradient);
        canvas.drawArc(mRectF, 0, 180, false, mShaderPaint);
    }
}