package com.archermind.demotest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Observable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.archermind.demotest.R;

import java.util.concurrent.TimeUnit;


/**
 * Toast工具类
 */
public class ToastUtils {
    private static ToastUtils mToastUtils;
    private static Toast mToast;
    private static Context mContext;

    @SuppressLint("ShowToast")
    private ToastUtils(Context context) {
        if (null == mToast) {
            mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_LONG);
        }
    }

    public static ToastUtils getInstance(Context context) {
        if (mToastUtils == null) {
            mContext = context.getApplicationContext();
            mToastUtils = new ToastUtils(context.getApplicationContext());
        }
        return mToastUtils;
    }

    public static void showShortToast(String mString) {
        if (mToast == null) {
            return;
        }
        mToast.setText(mString);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }


    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            return;
        }


        //设置Toast显示位置，居中，向 X、Y轴偏移量均为0
//        mToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mToast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        //获取自定义视图
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_toast_view, null);

        TextView tvMessage = (TextView) view.findViewById(R.id.tv_message_toast);

        //设置文本
        tvMessage.setText(msg);
        //设置视图
        mToast.setView(view);
        mToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
//        mToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //设置显示时长
        mToast.setDuration(Toast.LENGTH_SHORT);
        //显示
        mToast.show();


    }


}
