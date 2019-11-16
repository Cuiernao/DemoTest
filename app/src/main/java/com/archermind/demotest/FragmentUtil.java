package com.archermind.demotest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtil {

    public static void replaceFragment(FragmentManager FM, Fragment fragment, int LayoutId) {

        FragmentTransaction mfragmenttransaction = FM.beginTransaction();
        mfragmenttransaction.replace(LayoutId, fragment);
        mfragmenttransaction.commit();
    }

    public static void addFragment(FragmentManager FM, Fragment fragment, int LayoutId) {

        FragmentTransaction mfragmenttransaction = FM.beginTransaction();
        mfragmenttransaction.add(LayoutId, fragment);
        mfragmenttransaction.commit();
    }
}
