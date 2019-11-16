package com.archermind.demotest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.archermind.demotest.R;

/**
 * 设置资源
 */
public class TabLayoutView extends View {
    private Bitmap dial;//拨号
    private Bitmap people;//联系人
    private Bitmap search;//检索
    private Bitmap bg;//背景
    private int mBGWidth;
    private int mBGHeight;
    private Paint mPaint;
    private int flag = -1;
    public final static int DIAL_SHOW = 1;
    public final static int PEOPLE_SHOW = 2;
    public final static int SEARCH_SHOW = 3;

    public TabLayoutView(Context context) {
        this(context, null);
        initBitmap();
    }

    public TabLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initBitmap();
    }

    public TabLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmap();
    }

    private void initBitmap() {
        dial = BitmapFactory.decodeResource(getResources(), R.mipmap.btphone_top_icon_dial_1_nor, null);
        people = BitmapFactory.decodeResource(getResources(), R.mipmap.btphone_top_icon_people_1_nor, null);
        search = BitmapFactory.decodeResource(getResources(), R.mipmap.btphone_top_icon_search_1_nor, null);
        bg = BitmapFactory.decodeResource(getResources(), R.mipmap.toolbar_line, null);
        mPaint = new Paint();
        mBGWidth = bg.getWidth();
        mBGHeight = bg.getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (flag) {
            case DIAL_SHOW:
                canvas.drawBitmap(bg, 0, 0, mPaint);
                break;
            case PEOPLE_SHOW:
                canvas.drawBitmap(bg, mBGWidth, 0, mPaint);
                break;
            case SEARCH_SHOW:
                canvas.drawBitmap(bg, mBGWidth * 2, 0, mPaint);
                break;
        }
        int dialX = (mBGWidth - dial.getWidth()) / 2;
        int dialY = (mBGHeight - dial.getHeight()) / 2;
        int peopleX = dialX + mBGWidth;
        int searchX = dialX + mBGWidth * 2;
        canvas.drawBitmap(dial, dialX, dialY, mPaint);
        canvas.drawBitmap(people, peopleX, dialY, mPaint);
        canvas.drawBitmap(search, searchX, dialY, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //重新测量尺寸
        setMeasuredDimension(mBGWidth * 3, mBGHeight);
    }

    /**
     * 事件分发
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //判断手势
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取点击的x坐标，以此来判断选中的字母
                float x = event.getX();
                if (x > 0 && x < mBGWidth) {
                    //拨号
                    flag = DIAL_SHOW;

                }

                if (x > mBGWidth && x < mBGWidth * 2) {
                    //联系人
                    flag = PEOPLE_SHOW;

                }
                if (x > mBGWidth * 2 && x < mBGWidth * 3) {
                    //检索
                    flag = SEARCH_SHOW;
                }
                if (pageChangeListener != null) {
                    pageChangeListener.getPageChange(flag);
                }

                Log.d("layout-->", "dispatchTouchEvent: x" + x + " flag:" + flag);
                invalidate();
                break;

        }
        return true;
    }

    /**
     * 设置页面显示
     *
     * @param flag
     */
    public void setPageShow(int flag) {
        this.flag = flag;
        invalidate();
        if (pageChangeListener != null) {
            pageChangeListener.getPageChange(flag);
        }
    }

    /**
     * 页面变化监听
     *
     * @param pageChangeListener
     */
    public void setPageChangeListener(PageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    private PageChangeListener pageChangeListener;

    public interface PageChangeListener {
        void getPageChange(int flag);
    }
}
