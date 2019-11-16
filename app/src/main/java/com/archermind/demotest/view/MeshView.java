package com.archermind.demotest.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.archermind.demotest.R;

public class MeshView extends View {
    private Bitmap bitmap;
    //定义两个常量，指定该图片横向、纵向被划分为20格
    private final int WIDTH = 20;
    private final int HEIGHT = 3;
    //计录图片上包含441个顶点
    private final int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    //定义一个数组，保存Bitmap上21 * 21个点坐标
    private final float[] verts = new float[COUNT * 2];
    //定义一个数组，记录Bitmap上的20 * 20个点经过扭曲后的坐标
    private final float[] orig = new float[COUNT * 2];

    private float bitmapWidth;
    private float bitmapHeight;
    private Context mContext;
    private float WAVE_HEIGHT = 30;

    private double currentAngle = -0;
    private String TAG = "MeshView---->";
    private ValueAnimator mCycAnimator;
    private Float angle = 0f;
    private long mUpDurationTime = 500;
    private long mCycDurationTime = 2000;
    private ValueAnimator mUpAnimator;
    private Float currentWAVE_HEIGHT = -1f;

    public MeshView(Context context) {
        this(context, null);
    }

    public MeshView(Context context, AttributeSet attrs) {
        this(context, null, 0);

    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmap(context);
        initAnim();
        initUpAnim();
    }

    private void initBitmap(Context context) {
        mContext = context;
        setFocusable(true);
        //根据指定资源加载图片
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.point_up_00);
        //获取图片宽度，高宽度
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = bitmapHeight * y / HEIGHT;

            for (int x = 0; x <= WIDTH; x++) {
                float fx = bitmapWidth * x / WIDTH;
                //初始化orig, verts两个数组。初始化后orig, verts 两个数组均匀地保存了20 * 21个点的x,y坐标
                orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                orig[index * 2 + 1] = verts[index * 2 + 1] = fy;
                index += 1;
            }
        }
    }

    private void initAnim() {
        mCycAnimator = ValueAnimator.ofFloat(360f, 0f);//起始位置在最低点
        mCycAnimator.setDuration(mCycDurationTime)
                .setRepeatCount(
                        ValueAnimator.INFINITE);
        mCycAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                angle = (Float) animation.getAnimatedValue();
                currentAngle = angle * Math.PI / 180;
                Log.v(TAG, "[angle] " + angle + " [currentAngle] " + currentAngle);
                invalidate();
            }
        });
        mCycAnimator.setInterpolator(new LinearInterpolator());// 匀速旋转


    }

    private void initUpAnim() {
        mUpAnimator = ValueAnimator.ofFloat(0f, WAVE_HEIGHT);//起始位置在最低点
        mUpAnimator.setDuration(mUpDurationTime);
        mUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentWAVE_HEIGHT = (Float) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate: [currentWAVE_HEIGHT] " + currentWAVE_HEIGHT);
                invalidate();
            }
        });
        mUpAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCycAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mUpAnimator.setInterpolator(new LinearInterpolator());// 匀速旋转


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) bitmapWidth, (int) bitmapHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //super.onDraw(canvas);
        //对Bitmap按verts数组进行扭曲
        //从从第一个点（由第5个参数0控制）开始扭曲
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
                //把每一个水平像素通过正弦公式转换成正弦曲线
                //WAVE_HEIGHT表示波峰跟波低的垂直距离，皱褶后会向上超过水平线，所以往下偏移WAVE_HEIGHT / 2
                //5表示波浪的密集度，表示波峰波谷总共有五个,对应上面左图的1,2,3,4,5
                //j就是水平像的X轴坐标
                //K决定正弦曲线起始点(x=0)点的Y坐标，k=0就是从波峰波谷的中间开始左->右绘制曲线
                float yOffset = 0;
                if (i < 2) {
                    if (currentAngle != -1) {//上部点变化曲线
                        yOffset = currentWAVE_HEIGHT * (float) Math.sin((float) j / WIDTH * Math.PI + currentAngle);
//                        Log.e(TAG, "onDraw: [yOffset up]" + yOffset);
                    } else {
                        yOffset = 0;
                    }
                } else {
                    if (currentAngle != -1) {// 下面曲线变化规律算法
                        yOffset = currentWAVE_HEIGHT / 2 * (float) Math.cos((float) j / WIDTH * Math.PI + currentAngle);
//                        Log.e(TAG, "onDraw: [yOffset down]" + yOffset);
                    } else {
                        yOffset = 0;
                    }
                }


                verts[(i * (WIDTH + 1) + j) * 2 + 1] = orig[(i * (WIDTH + 1) + j) * 2 + 1] + yOffset;//
            }
        }

        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);

//
        if (currentWAVE_HEIGHT == -1f) {
            mUpAnimator.start();
        }

    }

//    private void initLine(Canvas canvas) {
//        Paint mPaint = new Paint();
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setAntiAlias(true);
//        mPaint.setColor(Color.WHITE);
//        mPaint.setStrokeWidth(5);
//        for (int y = 0; y < COUNT; y++) {
//
//            canvas.drawPoint(orig[y * 2], orig[y * 2 + 1], mPaint);
//        }
//    }

    private void AnimatorPause() {
        mCycAnimator.pause();
    }

    private void AnimatorCancel() {
        mCycAnimator.cancel();
    }

    private void AnimatorEnd() {
        mCycAnimator.end();
    }
}
