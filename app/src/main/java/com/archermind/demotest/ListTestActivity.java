package com.archermind.demotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archermind.demotest.adpter.PhoneAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListTestActivity extends AppCompatActivity {

    @BindView(R.id.mRecyclView)
    RecyclerView mRecyclView;
    @BindView(R.id.father)
    RelativeLayout father;
    @BindView(R.id.Up)
    TextView Up;
    @BindView(R.id.Down)
    TextView Down;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    private List<String> list;
    private PhoneAdapter phoneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test);
        ButterKnife.bind(this);

        FragmentUtil.addFragment(getSupportFragmentManager(),new SearchLayoutFragment(),R.id.frameLayout);
//        iniRW();
//
//        BezierLayout headerView = new BezierLayout(this);
//        refreshLayout.setHeaderView(headerView);
//        refreshLayout.setPureScrollModeOn();
    }


    private void iniRW() {
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + "");
        }
        phoneAdapter = new PhoneAdapter(R.layout.item, list);

        mRecyclView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclView.setAdapter(phoneAdapter);
        mRecyclView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {//滑动到底部


                }

                if (!recyclerView.canScrollVertically(-1)) {//滑动到顶部


                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("cui", "onScrolled: [dx]" + dx);
            }
        });
//        final LayoutAnimationController controller = new LayoutAnimationController(getAnimationSetFromLeft());
    }

    int position = 0;

    @OnClick({R.id.Up, R.id.Down})
    public void onViewClicked(View view) {

        //获取可显示的第一项item
//        int firstPosition = layoutManager.findFirstVisibleItemPosition();
//        //获取可显示的最后一项item
//        int lastPosition = layoutManager.findLastVisibleItemPosition();
        switch (view.getId()) {
            case R.id.Up:
//                if (firstPosition != 0) {
//                    layoutManager.scrollToPosition(firstPosition - 1);
//                } else {
//                    String lastString = list.get(list.size() - 1);
//                    list.add(0, lastString);
//                    list.remove(list.size() - 1);
//                    phoneAdapter.setNewData(list);
//
//                }

                if (position > 0) {
                    position--;
                    mRecyclView.smoothScrollToPosition(position);
//                    layoutManager.scrollToPosition(position);

                }
                break;
            case R.id.Down:
                if (position < list.size() - 1) {
                    position++;
                    mRecyclView.smoothScrollToPosition(position);

                }

//                if (lastPosition < list.size() - 1) {
//                    layoutManager.scrollToPosition(lastPosition + 1);
//                } else {
//                    String firstString = list.get(0);
//                    list.add(list.size(), firstString);
//                    list.remove(0);
//                    phoneAdapter.setNewData(list);
//                }
                break;
        }
    }
//
//    public void setRecyclerViewAnim() {
//        Log.e("cui", "onAnimationUpdate: setRecyclerViewAnim");
//        ValueAnimator transAnimator = ValueAnimator.ofFloat(0, 100);
////        transAnimator.setInterpolator(new DecelerateInterpolator());
//        transAnimator.setDuration(1000);
//        transAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int currentValue = (int) (float) animation.getAnimatedValue();
////                layoutManager.scrollToPositionWithOffset(0, currentValue);
//            }
//        });
//
//        transAnimator.start();
//    }

//    public static AnimationSet getAnimationSetFromLeft() {
//        AnimationSet animationSet = new AnimationSet(true);
//        TranslateAnimation translateX1 = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0,
//                RELATIVE_TO_SELF, -0.1f, RELATIVE_TO_SELF, 0.1f);
//        translateX1.setDuration(300);
//        translateX1.setInterpolator(new DecelerateInterpolator());
//        translateX1.setStartOffset(0);
//
//        TranslateAnimation translateX2 = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0,
//                RELATIVE_TO_SELF, 0.1f, RELATIVE_TO_SELF, -0.1f);
//        translateX2.setStartOffset(300);
//        translateX2.setInterpolator(new DecelerateInterpolator());
//        translateX2.setDuration(50);
//        TranslateAnimation translateX1_ = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0,
//                RELATIVE_TO_SELF, -0.1f, RELATIVE_TO_SELF, 0.1f);
//        translateX1_.setDuration(300);
//        translateX1_.setInterpolator(new DecelerateInterpolator());
//        translateX1_.setStartOffset(0);
//
//        TranslateAnimation translateX2_ = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0,
//                RELATIVE_TO_SELF, 0.1f, RELATIVE_TO_SELF, -0.1f);
//        translateX2_.setStartOffset(300);
//        translateX2_.setInterpolator(new DecelerateInterpolator());
//        translateX2_.setDuration(50);
//
//        TranslateAnimation translateX3 = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0f,
//                RELATIVE_TO_SELF, -0.1f, RELATIVE_TO_SELF, 0);
//        translateX3.setStartOffset(350);
//        translateX3.setInterpolator(new DecelerateInterpolator());
//        translateX3.setDuration(50);
//
//        animationSet.addAnimation(translateX1);
//        animationSet.addAnimation(translateX2);
//        animationSet.addAnimation(translateX3);
//        animationSet.setDuration(400);
//
//
//        return animationSet;
//    }

    /**
     * 从顶部进入
     *
     * @return
     */
//    public static AnimationSet getAnimationSetFromTop() {
//        AnimationSet animationSet = new AnimationSet(true);
//        TranslateAnimation translateX1 = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0,
//                RELATIVE_TO_SELF, -0.1f, RELATIVE_TO_SELF, 0.1f);
//        translateX1.setDuration(300);
//        translateX1.setInterpolator(new DecelerateInterpolator());
//        translateX1.setStartOffset(0);
//
//        TranslateAnimation translateX2 = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0,
//                RELATIVE_TO_SELF, 0.1f, RELATIVE_TO_SELF, -0.1f);
//        translateX2.setStartOffset(300);
//        translateX2.setInterpolator(new DecelerateInterpolator());
//        translateX2.setDuration(50);
//
//        TranslateAnimation translateX3 = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0f,
//                RELATIVE_TO_SELF, -0.1f, RELATIVE_TO_SELF, 0);
//        translateX3.setStartOffset(350);
//        translateX3.setInterpolator(new DecelerateInterpolator());
//        translateX3.setDuration(50);
//
//        animationSet.addAnimation(translateX1);
//        animationSet.addAnimation(translateX2);
//        animationSet.addAnimation(translateX3);
//        animationSet.setDuration(400);
//
//        return animationSet;
//    }

//    @NonNull
//    private static TranslateAnimation getTranslateAnimation(float v, float v2, long durationTime) {
//        TranslateAnimation translateX13 = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0,
//                RELATIVE_TO_SELF, v, RELATIVE_TO_SELF, v2);
//        translateX13.setDuration(durationTime);
//        translateX13.setInterpolator(new DecelerateInterpolator());
//        translateX13.setStartOffset(0);
//        return translateX13;
//    }
//
//    public static void setSmartCardFromBottom(Context context, RecyclerView layout) {
//        AnimationSet animationSet = new AnimationSet(true);
//        TranslateAnimation translate = new TranslateAnimation(RELATIVE_TO_SELF, 0, RELATIVE_TO_SELF, 0,
//                RELATIVE_TO_SELF, 1, RELATIVE_TO_SELF, 0);
//        translate.setDuration(300);
//        translate.setStartOffset(0);
//
//        AlphaAnimation animation = new AlphaAnimation(0, 1);
//        animation.setDuration(600);
//        animation.setStartOffset(0);
//
//        ScaleAnimation scaleAnimation = new ScaleAnimation(1.35f, 1.0f, 1.35f, 1.0f);
//        scaleAnimation.setDuration(600);
//        scaleAnimation.setStartOffset(0);
//
//        animationSet.addAnimation(translate);
//        animationSet.addAnimation(animation);
//        animationSet.addAnimation(scaleAnimation);
//        animationSet.setDuration(600);
//
//        LayoutAnimationController controller = new LayoutAnimationController(animationSet);
//        layout.setLayoutAnimation(controller);
//
//    }

//    float x1, y1, x2, y2;
//    //是不是向上滑动
//    boolean isMovinfToUp = false;
//    //是不是向下
//    boolean isMovinfToDown = false;
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        isMovinfToUp = false;
//        isMovinfToDown = false;
//        //继承了Activity的onTouchEvent方法，直接监听点击事件
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            //当手指按下的时候
//            x1 = event.getX();
//            y1 = event.getY();
//        }
//        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            //当手指移动的时候
//            x2 = event.getX();
//            y2 = event.getY();
//            if (y1 - y2 > 50) {
//                Toast.makeText(ListTestActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
//                isMovinfToUp = true;
//            } else if (y2 - y1 > 50) {
//                Toast.makeText(ListTestActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
//                isMovinfToDown = true;
//            } else if (x1 - x2 > 50) {
//                Toast.makeText(ListTestActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
//            } else if (x2 - x1 > 50) {
//                Toast.makeText(ListTestActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
//            }
//        }
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            Log.i("Lgq", "sssssssll离开了lllll==");
////            updview(nowpersion);
//        }
//        return super.onTouchEvent(event);
////        return false;
//    }


}
