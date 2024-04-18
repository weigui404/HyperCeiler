/*
  * This file is part of HyperCeiler.

  * HyperCeiler is free software: you can redistribute it and/or modify
  * it under the terms of the GNU Affero General Public License as
  * published by the Free Software Foundation, either version 3 of the
  * License.

  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU Affero General Public License for more details.

  * You should have received a copy of the GNU Affero General Public License
  * along with this program.  If not, see <https://www.gnu.org/licenses/>.

  * Copyright (C) 2023-2024 HyperCeiler Contributions
*/
package com.sevtinge.hyperceiler.ui;

import android.app.backup.BackupManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fan.common.base.BaseSettingsActivity;
import com.sevtinge.hyperceiler.provider.SharedPrefsProvider;
import com.sevtinge.hyperceiler.ui.fragment.framework.OtherSettings;
import com.sevtinge.hyperceiler.ui.fragment.home.HomeDockSettings;
import com.sevtinge.hyperceiler.ui.fragment.home.HomeFolderSettings;
import com.sevtinge.hyperceiler.ui.fragment.home.HomeGestureSettings;
import com.sevtinge.hyperceiler.ui.fragment.sub.MultiActionSettings;
import com.sevtinge.hyperceiler.ui.fragment.various.AlertDialogSettings;
import com.sevtinge.hyperceiler.utils.Helpers;
import com.sevtinge.hyperceiler.utils.PrefsUtils;

import java.util.Set;

import fan.preference.Preference;
import fan.preference.PreferenceFragment;
import fan.preference.core.PreferenceFragmentCompat;

public abstract class SettingsActivity extends BaseSettingsActivity implements PreferenceFragment.OnPreferenceStartFragmentCallback {

    SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferenceChangeListener = (sharedPreferences, s) -> {
        Log.i("prefs", "Changed: " + s);
        requestBackup();
        Object val = sharedPreferences.getAll().get(s);
        String path = "";
        if (val instanceof String)
            path = "string/";
        else if (val instanceof Set<?>)
            path = "stringset/";
        else if (val instanceof Integer)
            path = "integer/";
        else if (val instanceof Boolean)
            path = "boolean/";
        getContentResolver().notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/" + path + s), null);
        if (!path.equals("")) getContentResolver().notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/pref/" + path + s), null);
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerObserver();
        initCreate();
        initActionBar();
    }

    public void initCreate() {}

    public void initActionBar() {
        getAppCompatActionBar().setDisplayHomeAsUpEnabled(!(this instanceof HyperCeilerTabActivity));
    }

    public void onStartSettingsForArguments(Preference preference, boolean isBundleEnable) {
        mProxy.onStartSettingsForArguments(SubSettings.class, preference, isBundleEnable);
    }

    @Override
    public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat preferenceFragment, @NonNull Preference preference) {
        boolean isBundleEnable = preferenceFragment instanceof OtherSettings ||
            preferenceFragment instanceof HomeDockSettings ||
            preferenceFragment instanceof HomeFolderSettings ||
            preferenceFragment instanceof AlertDialogSettings ||
            preferenceFragment instanceof HomeGestureSettings ||
            preferenceFragment instanceof MultiActionSettings;
        onStartSettingsForArguments(preference, isBundleEnable);
        return true;
    }

    private void registerObserver() {
        PrefsUtils.mSharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);
        Helpers.fixPermissionsAsync(getApplicationContext());
        registerFileObserver();
    }

    private void registerFileObserver() {
        try {
            FileObserver mFileObserver = new FileObserver(PrefsUtils.getSharedPrefsPath(), FileObserver.CLOSE_WRITE) {
                @Override
                public void onEvent(int event, String path) {
                    Helpers.fixPermissionsAsync(getApplicationContext());
                }
            };
            mFileObserver.startWatching();
        } catch (Throwable t) {
            Log.e("prefs", "Failed to start FileObserver!");
        }
    }

    public void requestBackup() {
        new BackupManager(getApplicationContext()).dataChanged();
    }
}
