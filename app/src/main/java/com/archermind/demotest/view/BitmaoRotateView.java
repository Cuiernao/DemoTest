package com.archermind.demotest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.archermind.demotest.R;


/**
 * Created by bingye on 2019/1/7.
 */
public class BitmaoRotateView extends View {

    private Bitmap mCarTuank;

    private Matrix matrix;

    public BitmaoRotateView(Context context) {
        super(context);
    }

    public BitmaoRotateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        mCarTuank = BitmapFactory.decodeResource(getResources(), R.mipmap.car_tuank, null);

    }

    private int currentDegree;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radio = (int) Math.hypot(mCarTuank.getHeight(), mCarTuank.getWidth());
        canvas.drawBitmap(adjustPhotoRotation(mCarTuank, currentDegree), getWidth() / 2 - radio, getHeight() / 2 - radio, null);


    }

    public void setCurrentDegree(int currentDegree) {
        this.currentDegree = currentDegree;
        invalidate();
    }

    /**
     * int radio = (int) Math.hypot(bm.getHeight(), bm.getWidth());
     * &#x751f;&#x6210;&#x65cb;&#x8f6c;&#x5927;&#x5c0f;&#x4e3a;(radio*2)*(radio*2)&#x5c3a;&#x5bf8;&#x56fe;&#x7247;&#x65cb;&#x8f6c;&#x4e2d;&#x5fc3;&#x70b9;&#x4e3a;&#x539f;&#x56fe;&#x7247;&#x53f3;&#x4e0a;&#x89d2;,&#x5373;&#x751f;&#x6210;&#x65cb;&#x8f6c;&#x56fe;&#x7247;&#x4e2d;&#x5fc3;&#x70b9;
     *
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

}
