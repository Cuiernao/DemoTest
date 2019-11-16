package com.archermind.demotest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * @创建者 鑫鱻
 * @描述 Android零基础入门到精通系列教程，欢迎关注微信公众号ShareExpert
 */
public class MyStackAdapter extends BaseAdapter {
    private Context mContext = null;
    private int[] mImageIds = null;

    public MyStackAdapter(Context context, int[] imageIds) {
        this.mContext = context;
        this.mImageIds = imageIds;
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 该方法返回的View代表了每个列表项
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if(null == convertView) {
            // 创建一个ImageView
            imageView = new ImageView(mContext);
            // 设置ImageView的缩放类型
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            // 为imageView设置布局参数
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            convertView = imageView;
        } else {
            imageView = (ImageView) convertView;
        }

        // 给ImageView设置图片资源
        imageView.setImageResource(mImageIds[position]);

        return imageView;
    }
}
 