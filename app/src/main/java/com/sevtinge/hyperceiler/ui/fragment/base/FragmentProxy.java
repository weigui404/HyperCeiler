package com.sevtinge.hyperceiler.ui.fragment.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.sevtinge.hyperceiler.utils.PrefsUtils;

import fan.preference.PreferenceManager;

public class FragmentProxy {

    private Fragment mFragment;

    public FragmentProxy(Fragment fragment) {
    }

    public void onCreatePreferences(PreferenceManager preferenceManager) {
        preferenceManager.setSharedPreferencesName(PrefsUtils.mPrefsName);
        preferenceManager.setSharedPreferencesMode(Context.MODE_PRIVATE);
        preferenceManager.setStorageDeviceProtected();
    }

}
