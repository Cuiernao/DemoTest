package com.archermind.demotest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.StackView;

import com.archermind.demotest.MyStackAdapter;
import com.archermind.demotest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StackViewActivity extends AppCompatActivity {

    @BindView(R.id.av_flipper)
    StackView mStackView;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.previous)
    Button previous;
    @BindView(R.id.auto)
    Button auto;
    public int[] imgIds = {R.mipmap.car,R.mipmap.car,R.mipmap.car,R.mipmap.car,R.mipmap.car};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_view);
        ButterKnife.bind(this);
        mStackView.setInAnimation(this , R.animator.anim_left_enter);//设置图片进入动画
        mStackView.setOutAnimation(this , R.animator.anim_left_exit);//设置图片出来动画
        MyStackAdapter adapter = new MyStackAdapter(this, imgIds);
        mStackView.setAdapter(adapter);


    }

    @OnClick({R.id.next, R.id.previous, R.id.auto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.next:


                mStackView.showPrevious();

                break;
            case R.id.previous:
//                mStackView.setInAnimation(this , R.animator.anim_right_enter);
//                mStackView.setOutAnimation(this , R.animator.anim_right_exit);
                mStackView.showNext();

                break;
            case R.id.auto:

                break;
        }
    }
}
