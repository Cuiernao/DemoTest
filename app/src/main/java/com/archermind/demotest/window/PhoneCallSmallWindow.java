package com.archermind.demotest.window;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.archermind.demotest.R;


/**
 * 小窗口界面
 *
 * @author zjp
 * @since 2019.05.14
 */
public class PhoneCallSmallWindow extends BaseWindow {

    private final static String TAG = "SVBt-PhoneCallSmall";


    private View view;

    private Handler handler = new Handler();

    public PhoneCallSmallWindow(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.phone_small_notification_layout, null);
        initWindowParams();
    }

    private void initWindowParams() {
        mWindowParams.type =2024;
        mWindowParams.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        mWindowParams.x = 0;  //设置主窗体先对屏幕左侧的距离
        mWindowParams.y = 12;  //设置主窗体先对屏幕左侧的距离
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = 100;
        //mWindowParams.windowAnimations = R.style.PopMsgAni;
    }


    @Override
    public void show() {
        if (view == null) {
            Log.e(TAG, "[show] view is null , so return");
            return;
        }

            if (!view.isShown())
                mWindowManager.addView(view, mWindowParams);

    }

    @Override
    public void hide() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (view != null && view.isShown()) {
            Log.d(TAG, "[hide] real hide");
            mWindowManager.removeView(view);
        }

    }

    /**
     * @return
     */
    public boolean isShown() {
        if (view != null && view.isShown())
            return true;
        return false;
    }




}
