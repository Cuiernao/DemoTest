package com.archermind.demotest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.archermind.demotest.R;
import com.archermind.demotest.view.ColorProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends AppCompatActivity implements ColorProgressBar.ProgressChangeListener {


    @BindView(R.id.SeekBar1)
    ColorProgressBar SeekBar1;
    @BindView(R.id.SeekBar2)
    ColorProgressBar SeekBar2;
    @BindView(R.id.SeekBar3)
    ColorProgressBar SeekBar3;
    @BindView(R.id.SeekBar4)
    ColorProgressBar SeekBar4;
    @BindView(R.id.SeekBar5)
    ColorProgressBar SeekBar5;
    @BindView(R.id.SeekBar6)
    ColorProgressBar SeekBar6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        SeekBar1.setOnProgressChangeListener(this);
        SeekBar2.setOnProgressChangeListener(this);
        SeekBar3.setOnProgressChangeListener(this);
        SeekBar4.setOnProgressChangeListener(this);
        SeekBar5.setOnProgressChangeListener(this);
        SeekBar5.setOnProgressChangeListener(this);


    }

    @Override
    public void onProgressChanged(int currentProgress, boolean isUser, View view) {
        switch (view.getId()) {
            case R.id.SeekBar1:
                break;
            case R.id.SeekBar2:
                break;
            case R.id.SeekBar3:
                break;
            case R.id.SeekBar4:
                break;
            case R.id.SeekBar5:
                break;
            case R.id.SeekBar6:
                break;

        }
    }

    @Override
    public void onProgressUp(int currentProgress, View view) {

    }
}
