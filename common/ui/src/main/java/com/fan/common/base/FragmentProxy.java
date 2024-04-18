package com.fan.common.base;

import android.content.Context;

import androidx.fragment.app.Fragment;

import fan.preference.PreferenceManager;

public class FragmentProxy {

    public static String PREF_NAME = "hyperceiler_prefs";

    private Fragment mFragment;

    public FragmentProxy(Fragment fragment) {
    }

    public void onCreatePreferenceManager(PreferenceManager preferenceManager) {
        preferenceManager.setSharedPreferencesName(PREF_NAME);
        preferenceManager.setSharedPreferencesMode(Context.MODE_PRIVATE);
        preferenceManager.setStorageDeviceProtected();
    }

}
