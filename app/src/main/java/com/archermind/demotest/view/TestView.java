package com.archermind.demotest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TtsSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestView extends View {

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String str = "你好，我是\\n张宏";
        Spannable WordtoSpan = new SpannableString(str);
        WordtoSpan.setSpan(new AbsoluteSizeSpan(50), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        WordtoSpan.setSpan(new AbsoluteSizeSpan(14), 2, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(30);

        canvas.drawText(str, 0, str.length() - 1, 0, 50, mPaint);

    }
}
