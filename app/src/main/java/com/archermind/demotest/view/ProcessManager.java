package com.archermind.demotest.view;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 *
 * @author Hsueh
 * @email hsueh@onetos.cc
 * @date 2017-12-25  16:14
 */

public class ProcessManager {

    private static List<ImageView> mObjects;

    private static Map<ImageView,Process> mProcessMap;

    private static ProcessManager mInstance;

    public static ProcessManager get(){
        if (mInstance == null) {
            synchronized (ProcessManager.class){
                if (mInstance == null) {
                    mInstance = new ProcessManager();
                    mObjects = new ArrayList<>();
                    mProcessMap = new HashMap<>(200);
                }
            }
        }
        return mInstance;
    }

    public Map<ImageView,Process> getmProcessMap(){
        return mProcessMap;
    }

    public List<ImageView> getmObjects(){
        return mObjects;
    }

}
