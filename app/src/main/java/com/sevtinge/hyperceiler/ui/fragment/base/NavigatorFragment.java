package com.sevtinge.hyperceiler.ui.fragment.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fan.common.base.BasePreferenceFragment;
import com.fan.common.base.FragmentProxy;

import fan.preference.PreferenceFragment;

public abstract class NavigatorFragment extends SettingsPreferenceFragment {

    public abstract int getContentResId();

    @Override
    public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String s) {
        super.onCreatePreferences(bundle, s);
        if (getContentResId() != -1) {
            Bundle args = new Bundle();
            args.putInt("contentResId", getContentResId());
            setArguments(args);
        }
    }
}
