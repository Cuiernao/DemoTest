package com.archermind.demotest.view;


import android.widget.ImageView;

import com.archermind.demotest.view.Process;
import com.archermind.demotest.view.ProcessManager;

import java.util.Map;

/**
 * Create with Android Studio.
 *
 * @author Hsueh
 * @email hsueh@onetos.cc
 * @date 2017/12/24 14:24
 */

public class FrameLoader {

    public static Process into(ImageView tag){
        return Process.build(tag);
    }

    public static Process getProcessByTag(ImageView tag){
        Map<ImageView, Process> imageViewProcessMap = ProcessManager.get().getmProcessMap();
        if (imageViewProcessMap != null) {
            return imageViewProcessMap.get(tag);
        }
        return null;
    }

}
