package com.archermind.demotest.activity.ui.AbllumView;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;

/**
 * 处理置顶位置滑动
 */
public class TopSmoothScroller extends LinearSmoothScroller {
    public TopSmoothScroller(Context context) {
        super(context);
    }

    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_START;//具体见源码注释
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;//具体见源码注释
    }
}
