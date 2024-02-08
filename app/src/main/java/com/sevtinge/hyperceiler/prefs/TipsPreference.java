package com.sevtinge.hyperceiler.prefs;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sevtinge.hyperceiler.R;
import com.sevtinge.hyperceiler.ui.MainActivityContextHelper;

import fan.preference.Preference;

public class TipsPreference extends Preference {

    MainActivityContextHelper mainActivityContextHelper;

    public TipsPreference(@NonNull Context context) {
        this(context, null);
    }

    public TipsPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipsPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setEnabled(false);
        setLayoutResource(R.layout.preference_tips);
        updateTips();
    }

    public void updateTips() {
        mainActivityContextHelper = new MainActivityContextHelper(getContext());
        String tip = mainActivityContextHelper.getRandomTip();
        setSummary("Tips: " + tip);
    }
}
