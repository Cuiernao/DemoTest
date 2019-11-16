package com.archermind.demotest.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.archermind.demotest.R;


/**
 * Created by bingye on 2019/1/7.
 */
public class MoveCircleView extends View {

    private Bitmap mCarTuank;
    private Paint mArcViewPaint;
    private Paint mCirqueBgPaint;
    private Paint mLinePaint;
    private Paint mDragPaint;
    private Paint mDragInnerPaint;
    private Paint mLightPaint;
    private Paint mLightPaintStroke;

    /**
     * 大圆边距
     */
    private float mCircleLeft = 35;
    private float mCircleTop = 35;
    private float mCircleRight = 515;
    private float mCircleBottom = 515;

    /**
     * 大圆半径
     */
    private double mRadius = (mCircleRight - mCircleLeft) / 2;

    /**
     * 圆心坐标
     */
    private double mCenterX = mCircleRight - mRadius;
    private double mCenterY = mCircleBottom - mRadius;

    /**
     * 检测按下到抬起时旋转的角度、圆球的角度
     */
    private double mCurrentAngle = 145;
    /**
     * 设置亮度区域
     */
    private float mLightAngle = 0;

    /**
     * 上次的角度
     */
    private double mLastAngle = 145;

    /**
     * 可触摸范围的起始角度以及旋转角度
     */
    private float mStartAngle = 145;

    private float mSweepAngle = 80;//最大为80

    private float mFatherAngle = 80;//底层最大角度

    private float mGragAngle = 80;//灰色区域值

    private int level = 16;//80*20%

    /**
     * 手指可移动范围最大最小值
     */
    private double mMinValidateTouchArcRadius;
    private double mMaxValidateTouchArcRadius;


    private boolean mIsTouch;

    /**
     * 当前车窗开启百分比
     */
    private int mCurrentPercent;

    /**
     * 上次档位值
     */
    private double mLastAngleLevel = mStartAngle;

    /**
     * 触摸圆半径
     */
    private int mTouchRadius = 34;

    private MoveEventListener moveEventListener;
    /**
     * 设置的可滑动的最大百分百比
     */
    private int maxSweepPercent = 100;
    private float maxPercent = 100;

    public MoveCircleView(Context context) {
        super(context);
    }

    public MoveCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mCarTuank = BitmapFactory.decodeResource(getResources(), R.mipmap.car_tuank, null);
        mMinValidateTouchArcRadius = mRadius - 60;
        mMaxValidateTouchArcRadius = mRadius + 60;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArcView(canvas);
        drawDragBitmap(canvas);
        drawMoveArcView(canvas);
        drawLightView(canvas);
        drawCarTuankBitmap(canvas);
    }

    private void drawCarTuankBitmap(Canvas canvas) {
        int radio = (int) Math.hypot(mCarTuank.getHeight(), mCarTuank.getWidth());
        float bitMapAngle = mStartAngle + mLightAngle;
        //计算图片的夹角
        double tan = Math.atan2(mCarTuank.getHeight(), mCarTuank.getWidth());
        double angleA = 180 * tan / Math.PI;
        //旋转角度=旋转度数-bitmap夹角,
        canvas.drawBitmap(adjustPhotoRotation(mCarTuank, (int) (bitMapAngle - angleA)), (float) mCenterX - radio, (float) mCenterY - radio, null);

    }

    /**
     * 绘制亮区
     *
     * @param canvas
     */
    private void drawLightView(Canvas canvas) {
        RectF oval = new RectF(mCircleLeft, mCircleTop, mCircleRight, mCircleBottom);
        canvas.drawArc(oval, mStartAngle, mLightAngle, true, mLightPaint);
        canvas.drawArc(oval, mStartAngle, mLightAngle, true, mLightPaintStroke);

    }

    private void initPaint() {
        mArcViewPaint = new Paint();
        mArcViewPaint.setColor(Color.parseColor("#99021224"));
        mArcViewPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mArcViewPaint.setAntiAlias(true);

        mCirqueBgPaint = new Paint();
        mCirqueBgPaint.setColor(Color.parseColor("#99083a41"));
        mCirqueBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirqueBgPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#99083a41"));
        mLinePaint.setStrokeWidth(3);

        mDragPaint = new Paint();
        mDragPaint.setColor(Color.parseColor("#234772"));
        mDragPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDragPaint.setAntiAlias(true);

        mDragInnerPaint = new Paint();
        mDragInnerPaint.setColor(Color.parseColor("#99c5d1dc"));
        mDragInnerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDragInnerPaint.setAntiAlias(true);

        mLightPaint = new Paint();
        mLightPaint.setColor(Color.parseColor("#5918C0B2"));
        mLightPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLightPaint.setAntiAlias(true);
        mLightPaintStroke = new Paint();

        mLightPaintStroke.setColor(Color.parseColor("#FF21cdff"));
        mLightPaintStroke.setStyle(Paint.Style.STROKE);
        mLightPaintStroke.setStrokeWidth(3);
        mLightPaintStroke.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchArc(x, y)) {
                    // updateCurrentAngle(x, y);
                    mIsTouch = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsTouch) {
                    updateCurrentAngle(x, y);

                }
                break;
            case MotionEvent.ACTION_UP:
                mIsTouch = false;
                if (moveEventListener != null) {
                    mCurrentPercent = (int) ((mCurrentAngle - mStartAngle) * 100 / mSweepAngle);
                    moveEventListener.onActionUp(mCurrentPercent);
                }
                break;
        }
        invalidate();
        return true;

    }

    /**
     * 画一个圆弧
     */
    private void drawArcView(Canvas canvas) {
        RectF oval = new RectF(mCircleLeft, mCircleTop, mCircleRight, mCircleBottom);
        canvas.drawArc(oval, mStartAngle, mGragAngle, true, mArcViewPaint);
        float cirAngle = (float) ((mGragAngle + mStartAngle) / 180.0 * Math.PI);
        float left = (float) ((Math.cos(cirAngle)) * mRadius + mCenterX);
        float top = (float) ((Math.sin(cirAngle)) * mRadius + mCenterY);
        mLightPaint.setStrokeWidth(3);
        // 画线
        canvas.drawLine((float) mCenterX, (float) mCenterY, left, top, mLinePaint);
    }

    private float mTouchCircleX;
    private float mTouchCircleY;

    /**
     * 画触摸圆
     */
    private void drawDragBitmap(Canvas canvas) {
        float cirAngle = (float) (mCurrentAngle / 180.0 * Math.PI);
        float left = (float) ((Math.cos(cirAngle)) * mRadius + mCenterX);
        float top = (float) ((Math.sin(cirAngle)) * mRadius + mCenterY);
        mTouchCircleX = left;
        mTouchCircleY = top;
        // 画线
        canvas.drawLine((float) mCenterX, (float) mCenterY, left, top, mLinePaint);
        // canvas.drawBitmap(mDragBitmap, left - 20, top - 20, null);
//        canvas.drawCircle(left, top, 20, mDragPaint);
        canvas.drawCircle(left, top, mTouchRadius, mDragInnerPaint);

    }

    /**
     * 画滑动过的扇形区域
     */
    private void drawMoveArcView(Canvas canvas) {
        RectF oval = new RectF(mCircleLeft + 55, mCircleTop + 55, mCircleRight - 55, mCircleBottom - 55);
        float sweep = (float) mCurrentAngle - mStartAngle;

        if (sweep > mSweepAngle) {
            sweep = mSweepAngle;
        }
        canvas.drawArc(oval, mStartAngle, sweep, true, mCirqueBgPaint);
    }

    /**
     * 判断是否满足滑动条件
     * 判断档位距离起始位置是否< 20%(16度)，是，则回弹；不是，则确定最近档位
     */
    private boolean sweepEnable() {
        boolean enable = false;
        double offset = Math.abs(mCurrentAngle - mLastAngle);

        int n = ((int) (mCurrentAngle - mStartAngle)) / level; //计算在第几档位
        int unit = ((int) (mCurrentAngle - mStartAngle)) % level;//计算在偏移靠近档位的偏移量
        if (offset >= 16) {
            enable = true;
            if (unit < 8 && unit >= 0) {
                mCurrentAngle = n * level + mStartAngle;
            } else {
                mCurrentAngle = (n + 1) * level + mStartAngle;
            }
            mLastAngle = mCurrentAngle;
        }

        return enable;
    }

    /**
     * 不满足条件，回弹
     */
    private void sweepBack() {
        ValueAnimator animator = ValueAnimator.ofFloat((float) mCurrentAngle, (float) mLastAngle);//起始位置在最低点
        animator.setDuration((long) 1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float angle = (Float) animation.getAnimatedValue();
                mCurrentAngle = angle;
                invalidate();
            }
        });
        animator.start();
    }


    /**
     * 按下时判断按下的点是否按在圆边范围内
     *
     * @param x x坐标点
     * @param y y坐标点
     */
    private boolean isTouchArc(float x, float y) {
//        double d = getTouchRadius(x, y);
//        return d >= mMinValidateTouchArcRadius && d <= mMaxValidateTouchArcRadius;
        double d = getTouchRadiusInCircle(x, y);
        return d <= mTouchRadius;

    }

    /**
     * 计算某点到圆点的距离
     *
     * @param x x坐标点
     * @param y y坐标点
     */
    private double getTouchRadius(float x, float y) {
        double cx = x - mCenterX;
        double cy = y - mCenterY;
        return Math.hypot(cx, cy);
    }

    /**
     * 计算某点到可触摸圆圆点的距离
     *
     * @param x x坐标点
     * @param y y坐标点
     */
    private double getTouchRadiusInCircle(float x, float y) {
        double cx = x - mTouchCircleX;
        double cy = y - (mTouchCircleY);
        return Math.hypot(cx, cy);
    }

    /**
     * 更新当前进度对应弧度
     *
     * @param x 按下x坐标点
     * @param y 按下y坐标点
     */
    private void updateCurrentAngle(float x, float y) {
        //根据坐标转换成对应的角度
        double pointX = x - mCenterX;
        double pointY = y - mCenterY;
        double tan_x;//根据左边点所在象限处理过后的x值
        double tan_y;//根据左边点所在象限处理过后的y值
        double atan;//所在象限弧边angle


        //01：右上角区域
        if (pointX >= 0 && pointY <= 0) {
            tan_x = pointX;
            tan_y = pointY * (-1);
            atan = Math.atan(tan_x / tan_y);//求弧边
            mCurrentAngle = Math.toDegrees(atan) + 270.0;
        }

        //02：左上角区域
        if (pointX <= 0 && pointY <= 0) {
            tan_x = pointX * (-1);
            tan_y = pointY * (-1);
            atan = Math.atan(tan_y / tan_x);//求弧边
            mCurrentAngle = Math.toDegrees(atan) + 180.0;
        }

        //03：左下角区域
        if (pointX <= 0 && pointY >= 0) {
            tan_x = pointX * (-1);
            tan_y = pointY;
            atan = Math.atan(tan_x / tan_y);//求弧边
            mCurrentAngle = Math.toDegrees(atan) + 90.0;
        }

        //04：右下角区域
        if (pointX >= 0 && pointY >= 0) {
            tan_x = pointX;
            tan_y = pointY;
            atan = Math.atan(tan_y / tan_x);//求弧边
            mCurrentAngle = Math.toDegrees(atan);
        }

        // 限定角度范围，至允许在145-225度之间滑动
        if (mCurrentAngle < mStartAngle) {
            mCurrentAngle = mStartAngle;
        }
        if (mCurrentAngle > (mStartAngle + mSweepAngle)) {
            mCurrentAngle = (mStartAngle + mSweepAngle);
        }

        if (moveEventListener != null) {
            mCurrentPercent = (int) ((mCurrentAngle - mStartAngle) * 100 / mSweepAngle);
            moveEventListener.onActionMove(mCurrentPercent);
        }
    }

    /**
     * int radio = (int) Math.hypot(bm.getHeight(), bm.getWidth());*
     * @param bm
     * @param orientationDegree
     * @return
     */
    private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {

        Matrix m = new Matrix();
        //按照左上角原点位置旋转
        m.setRotate(orientationDegree);
        //计算旋转半径
        int radio = (int) Math.hypot(bm.getHeight(), bm.getWidth());
        //把原点平移到图片中心
        m.postTranslate(radio, radio);
        //创建最新图片魔板
        Bitmap bm1 = Bitmap.createBitmap(radio * 2, radio * 2, Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        //生成画布
        Canvas canvas = new Canvas(bm1);
        //绘制bitmap
        canvas.drawBitmap(bm, m, paint);

        return bm1;
    }

    public void setSweepValue(float sweepValue) {
        mSweepAngle = sweepValue;
    }

    public int getPercent() {
        return mCurrentPercent;
    }

    /**
     * 设置当前百分百 条件，设置的值小于目标值
     * mCurrentPercent = (int) ((mCurrentAngle - mStartAngle) * 100 / mSweepAngle);
     *
     * @param currentPercent
     */
    public void setCurrentPercent(int currentPercent) {
        if (currentPercent < 0) {
            currentPercent = 0;
        }
        if (currentPercent > maxSweepPercent) {
            currentPercent = maxSweepPercent;
        }
        mLightAngle = currentPercent * mSweepAngle / 100;
        invalidate();
    }

    /**
     * 设置最大可滑动百分百
     *
     * @param maxSweepPercent
     */
    public void setTargetPercent(int maxSweepPercent) {
        if (maxSweepPercent < 0) {
            maxSweepPercent = 0;
        }
        if (maxSweepPercent > 100) {
            maxSweepPercent = 100;
        }
        this.maxSweepPercent = maxSweepPercent;
        //计算最大可滑动角度
        mSweepAngle = maxSweepPercent * mFatherAngle / 100;
        invalidate();
    }

    /**
     * 设置灰色区域
     *
     * @param maxPercent
     */
    public void setMaxPercent(float maxPercent) {
        if (maxPercent < 0) {
            maxPercent = 0;
        }
        if (maxPercent > 100) {
            maxPercent = 100;
        }
        this.maxPercent = maxPercent;
        mGragAngle = maxPercent * mFatherAngle / 100;
        invalidate();
    }

    public void setOnMoveEventListener(MoveEventListener moveEventListener) {
        this.moveEventListener = moveEventListener;
    }

    public interface MoveEventListener {
        public void onActionMove(int percenter);

        public void onActionUp(int percenter);
    }
}
