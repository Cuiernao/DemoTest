package com.archermind.demotest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


import com.archermind.demotest.R;

import java.util.ArrayList;

/**
 * autour : lbing
 * date : 2018/7/30 0030 09:35
 * className :
 * version : 1.0
 * description :
 */


public class BarChartView extends View {

    private int barInterval;
    private int barWidth;
    private int top_text_size;
    private int bottom_text_size;
    private int bar_color;
    private int bottom_line_color;
    private int top_text_color;
    private int bottom_text_color;
    private Paint mTopTextPaint;
    private Paint mBottomTextPaint;
    private Paint mBarPaint;
    private Paint mLeftLinePaint;
    private Paint mAvcLinePaint;
    private ArrayList<BarData> innerData = new ArrayList<>();
    private int paddingTop;
    private int paddingLeft = 0;
    private int paddingBottom;
    private int paddingRight;
    private int defaultHeight = 440;
    private int bottom_view_height = 30;
    private int top_text_height = 30;
    private float scaleTimes = 1;
    private float lastX = 0;
    private float lastY = 0;
    private int measureWidth = 0;
    //这是最初的的位置
    private float startOriganalX = 0;
    private HorizontalScrollRunnable horizontalScrollRunnable;
    //临时滑动的距离
    private float tempLength = 0;
    private long startTime = 0;
    private boolean isFling = false;
    private float dispatchTouchX = 0;
    private float dispatchTouchY = 0;
    //是否到达边界
    private boolean isBoundary = false;
    private boolean isMove = false;

    private float mAvc;
    private int offset = 82;

    private Path mClipPath;

    private int[] mBarColors = {Color.parseColor("#275091"), Color.parseColor("#57a7bb")};//进度条颜色（渐变色的2个点）
    private int[] mAvcColors = {Color.parseColor("#275091"), Color.parseColor("#ffffff")};//进度条颜色（渐变色的2个点）

    public BarChartView(Context context) {
        this(context, null);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.barchar_style);
        barInterval = (int) typedArray.getDimension(R.styleable.barchar_style_barInterval, 12);
        bar_color = typedArray.getColor(R.styleable.barchar_style_bar_color, Color.parseColor("#f9e5c9"));
        barWidth = (int) typedArray.getDimension(R.styleable.barchar_style_barWidth, 18);
        top_text_size = (int) typedArray.getDimension(R.styleable.barchar_style_top_text_size, 8);
        top_text_color = typedArray.getColor(R.styleable.barchar_style_top_text_color, Color.parseColor("#00ff00"));
        bottom_text_size = (int) typedArray.getDimension(R.styleable.barchar_style_bottom_text_size, 20);
        bottom_text_color = typedArray.getColor(R.styleable.barchar_style_bottom_text_color, Color.parseColor("#ffffff"));
        bottom_line_color = typedArray.getColor(R.styleable.barchar_style_bottom_line_color, Color.parseColor("#ffffff"));
        typedArray.recycle();
        initPaint();
    }


    private void initPaint() {
        mClipPath = new Path();

        mTopTextPaint = new Paint();
        mTopTextPaint.setTextSize(top_text_size);
        mTopTextPaint.setColor(top_text_color);
        mTopTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTopTextPaint.setStyle(Paint.Style.FILL);
        mTopTextPaint.setDither(true);

        mBottomTextPaint = new Paint();
        mBottomTextPaint.setTextSize(bottom_text_size);
        mBottomTextPaint.setColor(bottom_text_color);
        mBottomTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mBottomTextPaint.setStyle(Paint.Style.FILL);
        mBottomTextPaint.setDither(true);


        mBarPaint = new Paint();
        //设置渐变色区域
        //        mBarPaint.setColor(bar_color);
        mBarPaint.setStrokeCap(Paint.Cap.ROUND);
        mBarPaint.setStyle(Paint.Style.FILL);
        mBarPaint.setDither(true);


        mLeftLinePaint = new Paint();
        mLeftLinePaint.setColor(Color.parseColor("#ffffff"));
        mLeftLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLeftLinePaint.setStyle(Paint.Style.FILL);
        mLeftLinePaint.setDither(true);
        //设置底部线的宽度
        mLeftLinePaint.setStrokeWidth(1.0f);


        mAvcLinePaint = new Paint();
        mAvcLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mAvcLinePaint.setStyle(Paint.Style.FILL);
    }

    public void setBarChartData(ArrayList<BarData> innerData, float avc, boolean isMonthData) {
        this.innerData.clear();
        this.innerData.addAll(innerData);
        mAvc = avc;
        scaleTimes = (float) ((defaultHeight - bottom_view_height) / (Math.ceil(getMaxValue()) + 3));//16;
        startOriganalX = 0;
        invalidate();
    }

    private float getMaxValue() {
        float defaultValue = 0;
        if (innerData.size() > 0) {
            defaultValue = innerData.get(0).getValue();
            for (int i = 0; i < innerData.size(); i++) {
                if (innerData.get(i).getValue() > defaultValue) {
                    defaultValue = innerData.get(i).getValue();
                }
            }
        }
        return defaultValue;
    }

    //进行滑动的边界处理
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("TAG", "MyBarChartView===dispatchTouchEvent==" + ev.getAction());
        int dispatchCurrX = (int) ev.getX();
        int dispatchCurrY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //父容器不拦截点击事件，子控件拦截点击事件。如果不设置为true,外层会直接拦截，从而导致motionEvent为cancle
                getParent().requestDisallowInterceptTouchEvent(true);
                dispatchTouchX = getX();
                dispatchTouchY = getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = dispatchCurrX - dispatchTouchX;
                float deltaY = dispatchCurrY - dispatchTouchY;
                if (Math.abs(deltaY) - Math.abs(deltaX) > 0) {//竖直滑动的父容器拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                //这是向右滑动，如果是滑动到边界，那么就让父容器进行拦截
                if ((dispatchCurrX - dispatchTouchX) > 0 && startOriganalX == 0) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else if ((dispatchCurrX - dispatchTouchX) < 0 && startOriganalX == -getMoveLength()) {//这是向右滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        dispatchTouchX = dispatchCurrX;
        dispatchTouchY = dispatchCurrY;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isBoundary = false;
        isMove = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                startTime = System.currentTimeMillis();
                //当点击的时候，判断如果是在fling的效果的时候，就停止快速滑动
                if (isFling) {
                    removeCallbacks(horizontalScrollRunnable);
                    isFling = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float currX = event.getX();
                float currY = event.getY();
                startOriganalX += currX - lastX;

                //这是向右滑动
                if ((currX - lastX) > 0) {
                    Log.e("TAG", "向右滑动");
                    if (startOriganalX > 0) {
                        startOriganalX = 0;
                        isBoundary = true;
                    }

                } else {//这是向左滑动
                    Log.e("TAG", "向左滑动");
                    if (-startOriganalX > getMoveLength()) {
                        startOriganalX = -getMoveLength();
                        isBoundary = true;
                    }
                }
                tempLength = currX - lastX;
                //如果数据量少，根本没有充满横屏，就没必要重新绘制，
                if (measureWidth < innerData.size() * (barWidth + barInterval)) {
                    invalidate();
                }

                lastX = currX;
                lastY = currY;
                break;
            case MotionEvent.ACTION_UP:
                long endTime = System.currentTimeMillis();
                //计算猛滑动的速度，如果是大于某个值，并且数据的长度大于整个屏幕的长度，那么就允许有flIng后逐渐停止的效果
                float speed = tempLength / (endTime - startTime) * 1000;
                if (Math.abs(speed) > 100 && !isFling && measureWidth < innerData.size() * (barWidth + barInterval)) {
                    this.post(horizontalScrollRunnable = new HorizontalScrollRunnable(speed));
                }
                isMove = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                isMove = false;
                break;
        }
        return true;
    }

//    @Override
//    public void onDrawForeground(Canvas canvas) {
//        super.onDrawForeground(canvas);
//        mClipPath.reset();
//                mClipPath.addRect(55, defaultHeight-bottom_view_height-mAvc*scaleTimes,measureWidth,defaultHeight-bottom_view_height-mAvc*scaleTimes+1, Path.Direction.CW);
//        canvas.clipPath(mClipPath);
//          drawAvcLine(canvas);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        drawLeftLine(canvas);
        canvas.clipRect(new RectF(offset, 0, measureWidth, defaultHeight));
        mClipPath = new Path();
        mClipPath.addRect(55, defaultHeight - bottom_view_height - mAvc * scaleTimes, measureWidth, defaultHeight - bottom_view_height - mAvc * scaleTimes + 1, Path.Direction.CW);
        canvas.clipPath(mClipPath, Region.Op.UNION);
//        canvas.drawPath(mClipPath,mBottomTextPaint);

        //如果没有数据 绘制loading...

        if (innerData.size() <= 0) {
            drawNoDataText(canvas);

        } else {
            int startX = (int) (offset + startOriganalX);
            int endY = defaultHeight - bottom_view_height;

            for (int i = 0; i < innerData.size(); i++) {

                int startY = defaultHeight - bottom_view_height - (int) (innerData.get(i).getValue() * scaleTimes);
                float topTextWidth = mTopTextPaint.measureText(innerData.get(i).getValue() + "");

                //绘制bar
                drawBar(canvas, startX, startY, endY);
                //绘制下面的文字
                float bottomTextWidth = mBottomTextPaint.measureText(innerData.get(i).bottomText);
                float bottomStartX = startX + barWidth / 2 - bottomTextWidth / 2;
                Rect rect = new Rect();
                mBottomTextPaint.getTextBounds(innerData.get(i).getBottomText(), 0, innerData.get(i).getBottomText().length(), rect);
                float bottomStartY = defaultHeight - bottom_view_height + 10 + rect.height();//rect.height()是获取文本的高度;
                //绘制底部的文字
                drawBottomText(canvas, innerData.get(i).getBottomText(), bottomStartX, bottomStartY);

                startX = startX + barWidth + barInterval;
            }

            drawAvcLine(canvas);
            canvas.restore();
        }

    }

    private void drawLeftLine(Canvas canvas) {
        canvas.drawLine(54, 0, 54, defaultHeight - bottom_view_height, mLeftLinePaint);
        canvas.drawLine(54, defaultHeight - bottom_view_height, 54 + 18, defaultHeight - bottom_view_height, mLeftLinePaint);
    }

    private void drawNoDataText(Canvas canvas) {
        String text = "loading...";
        float textWidth = mBottomTextPaint.measureText(text);
        canvas.drawText(text, measureWidth / 2 - textWidth / 2, defaultHeight / 2 - 10, mBottomTextPaint);
    }

    //绘制bar
    private void drawBar(Canvas canvas, int startX, int startY, int endY) {
        Rect mRect = new Rect(startX, startY, startX + barWidth, endY);
        LinearGradient shader = new LinearGradient(startX, endY, startX, startY, mBarColors, null,
                Shader.TileMode.CLAMP);
        mBarPaint.setShader(shader);
        canvas.drawRect(mRect, mBarPaint);

    }

    private void drawAvcLine(Canvas canvas) {
//        LinearGradient shader = new LinearGradient(0, defaultHeight-mAvc*scaleTimes, measureWidth, defaultHeight-mAvc*scaleTimes, mAvcColors, new float[]{0.95f,0.05f},
//                Shader.TileMode.CLAMP);
        mAvcLinePaint.setColor(Color.parseColor("#ffffff"));
        canvas.drawLine(0, defaultHeight - mAvc * scaleTimes, measureWidth, defaultHeight - mAvc * scaleTimes, mAvcLinePaint);
    }

    private void drawBottomText(Canvas canvas, String text, float bottomStartX, float bottomStartY) {
        if (text == "1" || Integer.valueOf(text) % 5 == 0) {
            canvas.drawText(text, bottomStartX, bottomStartY, mBottomTextPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth = width = widthSize;
        } else {
            width = getAndroiodScreenProperty().get(0);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            defaultHeight = height = heightSize;
        } else {
            height = defaultHeight;
        }
        defaultHeight = 440;
        setMeasuredDimension(width, defaultHeight);
        paddingTop = getPaddingTop();
//        paddingLeft = getPaddingLeft();
        paddingBottom = getPaddingBottom();
        paddingRight = getPaddingRight();

    }

    private ArrayList<Integer> getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(screenWidth);
        integers.add(screenHeight);
        return integers;
    }

    private int getMoveLength() {
        return (barWidth + barInterval) * innerData.size() - measureWidth + offset;
    }

    public boolean isBoundary() {
        return isBoundary;
    }

    public boolean isMove() {
        return isMove;
    }

    public static class BarData {
        private float value;
        private String bottomText;

        public BarData(float value, String bottomText) {
            this.value = value;
            this.bottomText = bottomText;
        }

        public float getValue() {
            return value;
        }

        public void setCount(int value) {
            this.value = value;
        }

        public String getBottomText() {
            return bottomText == null ? "" : bottomText;
        }

        public void setBottomText(String bottomText) {
            this.bottomText = bottomText;
        }
    }

    private class HorizontalScrollRunnable implements Runnable {

        private float speed = 0;

        public HorizontalScrollRunnable(float speed) {
            this.speed = speed;
        }

        @Override
        public void run() {
            if (Math.abs(speed) < 30) {
                isFling = false;
                return;
            }
            isFling = true;
            startOriganalX += speed / 15;
            speed = speed / 1.15f;
            //这是向右滑动
            if ((speed) > 0) {
                if (startOriganalX > 0) {
                    startOriganalX = 0;
                }

            } else {//这是向左滑动
                if (-startOriganalX > getMoveLength()) {
                    startOriganalX = -getMoveLength();
                }
            }
            postDelayed(this, 20);
            invalidate();
        }
    }
}