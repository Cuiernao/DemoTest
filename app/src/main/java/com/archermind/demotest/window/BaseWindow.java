package com.archermind.demotest.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * @author zjp
 * @since 2019.05.13
 */
public abstract class BaseWindow {

    protected Context context;

    protected WindowManager mWindowManager;
    protected WindowManager.LayoutParams mWindowParams;

    public BaseWindow() {
    }

    public BaseWindow(Context context) {
        this.context = context;
        initWindowParams();
    }

    /**
     * 初始化window参数
     */
    private void initWindowParams() {
        mWindowManager = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        mWindowParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
        mWindowParams.format = PixelFormat.TRANSPARENT;
        mWindowParams.gravity = Gravity.START | Gravity.BOTTOM;
    }

    protected abstract void show();

    protected abstract void hide();
}
