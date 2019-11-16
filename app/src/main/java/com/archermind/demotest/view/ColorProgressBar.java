package com.archermind.demotest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.icu.math.BigDecimal;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.archermind.demotest.R;


/**
 * Created by cui on 2019/1/7.
 */
public class ColorProgressBar extends View {
    private static final int TOUUS_WIDTH = 3;
    private Paint externalPaint;
    private Paint innerPaint;

    private Bitmap mBackgroundBitmap;
    private Bitmap mRoundBitmap;
    private Bitmap mCenterBitmap;

    private int mRadius;
    //当前进度
    private int mCurrentProcess;
    private int mMinProcess;
    private int mMaxProcess;
    private int mCircleX;
    private int mCircleY;
    private int innerColor;
    private int mColorX;
    //    private int mValue;//实际给MCU的值,范围0-63
    //设置进度条的高度
    private int mLineHeight = 20;

    private boolean mEnabled = true;
    private Paint mPaint;
    private ProgressChangeListener mProgressChangeListener;
    //进度条背景
    private int mBgWidth;
    private int mBgHeight;
    private int mRoundWdith;
    private int mRoundHeight;
    private boolean isPressDown = false;
    private int mLastProgress = 0;
    private int mBgcolor;

    public void setOnProgressChangeListener(ProgressChangeListener mProgressChangeListener) {
        this.mProgressChangeListener = mProgressChangeListener;
    }

    public ColorProgressBar(Context context) {
        super(context);
    }

    public ColorProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorProgressBar);
        //获取进度
        mCurrentProcess = array.getInt(R.styleable.ColorProgressBar_currentProcess, 0);
        mMinProcess = array.getInt(R.styleable.ColorProgressBar_minProcess, 0);
        mMaxProcess = array.getInt(R.styleable.ColorProgressBar_maxProcess, 100);
        mBgcolor = array.getColor(R.styleable.ColorProgressBar_bgColor, getContext().getResources().getColor(R.color.Blue));
        //配置显示区域
        int slideBackground = array.getInt(R.styleable.ColorProgressBar_slideBackground, R.drawable.custom_color_bg);
        //配置滑块资源
        int slideImg = array.getInt(R.styleable.ColorProgressBar_slideImg, R.drawable.rount);
        mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), slideBackground, null);
        mRoundBitmap = BitmapFactory.decodeResource(getResources(), slideImg, null);
        mCenterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.center, null);
        array.recycle();

        initData();
        mRadius = mBackgroundBitmap.getHeight() / 2;
        // 图片颜色对应的x值与背景x值相差一个半径
        mCircleX = mRoundHeight / 2;
        mCircleY = mRadius;
        //颜色的x
        mColorX = mCurrentProcess;
        Log.d("ColorBarView", "ColorBarView: mRadius =" + mRadius);
        innerColor = mBackgroundBitmap.getPixel(mColorX, mRadius);

        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBgcolor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制底层背景
        drawBackground(canvas);
        //绘制剪切区域
        drawBitmap(canvas);
        //绘制圆形按钮
        if (isPressDown) {
            //绘制发光外环
            canvas.drawBitmap(tintBitmap(mRoundBitmap, innerColor), mCircleX - mRoundWdith / 2, 0, null);
            //绘制内环
            canvas.drawBitmap(mCenterBitmap, mCircleX - mRoundWdith / 2, 0, null);
        }

    }

    /**
     * 剪切圆角图形
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    private Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, mCircleX - mRoundWdith / 2, bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 绘制当前进度
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        int left = mRoundHeight / 2;
        int top = (mRoundHeight - mLineHeight) / 2;
        int bottom = (mRoundHeight + mLineHeight) / 2;
        int right = mBgWidth + mRoundWdith / 2;

        RectF rectFBG = new RectF(left, top, right, bottom);
        //绘制背景
        canvas.drawRoundRect(rectFBG, 100, 100, mPaint);
    }

    private void initData() {

        mBgWidth = mBackgroundBitmap.getWidth();
        mBgHeight = mBackgroundBitmap.getHeight();
        mRoundWdith = mRoundBitmap.getWidth();
        mRoundHeight = mRoundBitmap.getHeight();
        mLineHeight = mBgHeight;
    }

    /**
     * 对图片着色
     *
     * @param inBitmap
     * @param tintColor
     * @return
     */
    public static Bitmap tintBitmap(Bitmap inBitmap, int tintColor) {
        if (inBitmap == null) {
            return null;
        }
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        return outBitmap;
    }


    /**
     * 绘制背景
     *
     * @param canvas
     */
    private void drawBitmap(Canvas canvas) {
        int top = (mRoundHeight - mBgHeight) / 2;
        canvas.drawBitmap(getRoundedCornerBitmap(mBackgroundBitmap, mLineHeight / 2 - 2), mRoundHeight / 2, top, null);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWdith = mBackgroundBitmap.getWidth() + mRoundBitmap.getWidth();
        setMeasuredDimension(viewWdith, mRoundBitmap.getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnabled) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_MOVE:
                isPressDown = true;
                int x = (int) event.getX();
                //获取图片颜色
                int left = mRoundHeight / 2;
                //计算平均数
                float processX = mBgWidth * 1f / (mMaxProcess - mMinProcess + 1);
                if (x <= left + processX) {
                    mCircleX = left;
                    mColorX = 0;
                    //当前最小
                    mCurrentProcess = mMinProcess;
                }
                if (x <= mBgWidth + mRoundWdith / 2 && x > left) {

                    mColorX = x - left;
                    mCircleX = x;
                    float f = (mMaxProcess - mMinProcess + 1) * mColorX * 1f / (mBgWidth - 5);

                    f = new BigDecimal(f).setScale(0, BigDecimal.ROUND_HALF_UP)
                            .floatValue();
                    Log.v("cui", "-->[f] " + f);
                    mCurrentProcess = (int) f;
                    if (mCurrentProcess < mMinProcess) {
                        mCurrentProcess = mMinProcess;
                    }
                }

                if (x > mBgWidth + mRoundWdith / 2) {
                    mCircleX = mBgWidth + mRoundWdith / 2;
                    //TODO 注意颜色边界
                    mColorX = mBgWidth - 5;
                    mCurrentProcess = mMaxProcess;
                }
                if (mColorX < mBgWidth) {
                    innerColor = mBackgroundBitmap.getPixel(mColorX, mCircleY);
                }
                if (mProgressChangeListener != null) {
                    //计算当前进度
                    Log.v("cui", "[mColorX} " + mColorX + " [mCurrentProcess]" + mCurrentProcess);
                    if (mLastProgress != mCurrentProcess) {//去重事件
                        mProgressChangeListener.onProgressChanged(mCurrentProcess, isPressDown, this);
                    }
                    //赋值给最后一次计算值
                    mLastProgress = mCurrentProcess;
                }

                break;
            case MotionEvent.ACTION_UP:
                isPressDown = false;

                if (mProgressChangeListener != null) {
                    mProgressChangeListener.onProgressUp(mCurrentProcess, this);
                }
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getCurrentProcess() {
        return mCurrentProcess;
    }

    /**
     * 设置最大值l
     */
    public void setMaxProcess(int mMaxProcess) {
        this.mMaxProcess = mMaxProcess;
        invalidate();
    }

    /**
     * 设置最小
     */
    public void setMinProcess(int mMinProcess) {
        this.mMinProcess = mMinProcess;
        invalidate();
    }

    /**
     * 设置当前值
     * mCurrentProcess = mMaxProcess * mColorX / (mBgWidth - 5);
     *
     * @param currentProcesse
     */
    public void setCurrentProcesse(int currentProcesse) {
        //根据当前进度计算当前颜色
        if (currentProcesse <= mMinProcess) {
            mCurrentProcess = mMinProcess;
        }
        if (currentProcesse > mMaxProcess) {
            mCurrentProcess = mMaxProcess;
        }
        mCurrentProcess = currentProcesse;
        //计算当前颜色值的标记
        float f = mCurrentProcess * (mBgWidth - 5) * 1f / (mMaxProcess - mMinProcess + 1);
        f = new BigDecimal(f).setScale(0, BigDecimal.ROUND_HALF_UP)
                .floatValue();
        mColorX = (int) f;
        mCircleX = (int) f + mRoundWdith / 2;

        //设置最大值
        if (mCurrentProcess == mMaxProcess) {//当等于最大值时补齐差值
            mColorX = mBgWidth - 5;
            mCircleX = mColorX + mRoundWdith / 2 + 5;

        }//设置最小值
        if (mCurrentProcess == mMinProcess) {//当等于最大值时补齐差值
            mColorX = 0;
            mCircleX = mColorX + mRoundWdith / 2;
        }

        Log.e("cui", "setCurrentProcesse: [mColorX]" + mColorX);
        if (mColorX < mBackgroundBitmap.getWidth()) {
            innerColor = mBackgroundBitmap.getPixel(mColorX, mRadius);
        }
        //主动设置
        if (mProgressChangeListener != null) {
            if (mLastProgress != mCurrentProcess) {
                mProgressChangeListener.onProgressChanged(mCurrentProcess, isPressDown, this);
            }
            mLastProgress = mCurrentProcess;
        }
        invalidate();
    }

    public void setEnabled(boolean enable) {
        mEnabled = enable;
    }

    /**
     * 进度监听器
     */
    public interface ProgressChangeListener {

        void onProgressChanged(int currentProgress, boolean isUser, View view);

        void onProgressUp(int currentProgress, View view);
    }

}