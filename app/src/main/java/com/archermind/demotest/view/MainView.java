package com.archermind.demotest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.archermind.demotest.R;

public class MainView extends View {
    //Camera类
    private Camera mCamera;

    private Bitmap face;
    private Matrix mMatrix = new Matrix();
    private Paint mPaint = new Paint();

    private int mLastMotionX, mLastMotionY;

    //图片旋转时的中心点坐标
    private int centerX, centerY;
    //转动的总距离，跟度数比例1:1
    private int deltaX, deltaY;
    //图片宽度高度
    private int bWidth, bHeight;

    public MainView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWillNotDraw(false);
        mCamera = new Camera();
        mPaint.setAntiAlias(true);
        face = BitmapFactory.decodeResource(getResources(), R.mipmap.btphone_bgparticle_1);
        bWidth = face.getWidth();
        bHeight = face.getHeight();
        centerX = bWidth >> 1;
        centerY = bHeight >> 1;
    }

    void rotate(int degreeX, int degreeY) {
        deltaX += degreeX;
        deltaY += degreeY;

        mCamera.save();
        mCamera.rotateY(deltaX);
        mCamera.rotateX(-deltaY);
        mCamera.translate(0, 0, -centerX);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();

        //以图片的中心点为旋转中心,如果不加这两句，就是以（0,0）点为旋转中心
        mMatrix.preTranslate(-centerX, -centerY);
        mMatrix.postTranslate(centerX, centerY);
        mCamera.save();

        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(face.getWidth(), face.getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastMotionX;
                int dy = y - mLastMotionY;
                rotate(dx, dy);
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawBitmap(face, mMatrix, mPaint);
    }
}