package com.archermind.demotest.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.archermind.demotest.R;

public class FliperAdapter extends BaseAdapter {

    private int[] imgIds;
    private Context context;

    public FliperAdapter(Context context, int[] imgIds) {
        this.imgIds = imgIds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgIds == null ? 0 : imgIds.length;
    }

    @Override
    public Object getItem(int position) {
        return imgIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolde holde = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fliper, parent, false);
            holde = new ViewHolde();
            holde.imageView = (ImageView) convertView.findViewById(R.id.item_img);
            convertView.setTag(holde);
        } else {
            holde = (ViewHolde) convertView.getTag();
        }
        holde.imageView.setImageResource(imgIds[position]);
        return convertView;
    }


    static class ViewHolde {
        public ImageView imageView;
    }
}
