package com.archermind.demotest.view;

import android.support.v4.util.Pools;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 *
 * @author Hsueh
 * @email hsueh@onetos.cc
 * @date 2017-12-25  15:46
 */

public class Process {
    /**
     * 播方动画的相应布局
     */
    private ImageView mImageView;
    /**
     * 播放动画的图片数组
     */
    private int[] mImageRes;
    /**
     * 是否需要停止
     */
    private boolean stop;
    /**
     * 记录播放状态
     */
    private boolean playing;
    /**
     * 当前播放第*张
     */
    private int pImageNo;
    /**
     * 图片刷新频率
     */
    private int pImageFrequency;
    /**
     * 是否无限循环播放
     */
    private boolean endlessLoop = false;
    /**
     * 循环次数，不定义默认为1
     */
    private int loopCount = 1;
    /**
     * 记录当前循环了几次
     */
    private int countCache;

    private String mTag = getClass().getName();

    private static Process mInstance;

    private static List<ImageView> mObjects ;

    private static Map<ImageView, Process> mProcessMap;

    private static Pools.SynchronizedPool<Process> sPool = new Pools.SynchronizedPool<>(20);


    /**
     * 创建新的对象
     *
     * @return
     */
    public static Process build(ImageView tag){
//        mTag = tag;
//        mObjects = ProcessManager.get().getmObjects();
//        mProcessMap =  ProcessManager.get().getmProcessMap();
//
//        Set<Map.Entry<ImageView, Process>> entries = mProcessMap.entrySet();
//        for (Map.Entry<ImageView, Process> entry : entries) {
//            if (entry.getKey().equals(mTag)) {
//                mInstance = mProcessMap.get(mTag);
//                mInstance.into(tag);
//                return mInstance;
//            }
//        }
//        mInstance = new Process();
//        ProcessManager.get().getmObjects().add(tag);
//        ProcessManager.get().getmProcessMap().put(tag, mInstance);
        mInstance = obtain();
        mInstance.into(tag);
        return mInstance;
    }

    public static Process obtain(){
        Process acquire = sPool.acquire();
        return (acquire != null) ? acquire : new Process();
    }

    public void recycle(){
        sPool.release(this);
    }

    /**
     * 设置动画的ImageView
     *
     * @param pImageView
     * @return
     */
    private Process into(ImageView pImageView) {
        stop = true;
        if (mImageView != null) {
            mImageView.removeCallbacks(mRunnable);
        }
        if (pImageView != mImageView) {
            stop();
        }
        this.mImageRes = null;
        this.mImageView = pImageView;
        return mInstance;
    }

    /**
     * 设置动画数组文件
     *
     * @param pImageRes
     * @return
     */
    public Process load(int[] pImageRes) {
        this.mImageRes = pImageRes;
        return mInstance;
    }



    /**
     * 开始播放
     *
     * @param pImageNo
     * @param frequency
     */
    public Process play(int pImageNo, int frequency) {
        stop = false;
        this.pImageNo = pImageNo;
        this.pImageFrequency = frequency;
        mImageView.postDelayed(mRunnable, frequency);
        return mInstance;
    }

    public Process play(int frequency) {
        play(0, frequency);
        return mInstance;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (stop) {
                playing = false;
                return;
            } else {
                mImageView.setImageResource(mImageRes[pImageNo]);
                if (pImageNo >= mImageRes.length - 1) {
                    countCache++;
                    if (endlessLoop) {
                        pImageNo = 0;
                        play(pImageNo, pImageFrequency);
                        playing = true;
                    } else {
                        if (countCache >= loopCount) {
                            playing = false;
                            stop();
                            return;
                        } else {
                            pImageNo = 0;
                            play(pImageNo, pImageFrequency);
                            playing = true;
                        }
                    }
                } else {
                    play(pImageNo + 1, pImageFrequency);
                    playing = true;
                }
            }
        }
    };

    public boolean isStop() {
        return stop;
    }

    private void setStop(boolean stop) {
        this.stop = stop;
    }

    /**
     * 停止播放
     */
    public void stop() {
        setStop(true);
        countCache = 0;
        if (mImageView != null) {
            mImageView.removeCallbacks(mRunnable);
            System.gc();
            recycle();
        }
        if (mProcessMap != null) {
            mProcessMap.clear();
        }
        if (mObjects != null) {
            mObjects.clear();
        }

    }

    public boolean isPlaying() {
        return playing;
    }

    /**
     * 是否无限循环
     */
    public Process setEndlessLoop(boolean endlessLoop) {
        this.endlessLoop = endlessLoop;
        return mInstance;
    }

    /**
     * 循环次数
     */
    public Process setLoopCount(int loopCount) {
        this.loopCount = loopCount;
        return mInstance;
    }


}
