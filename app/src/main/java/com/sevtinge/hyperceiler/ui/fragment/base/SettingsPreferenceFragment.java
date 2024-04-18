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
package com.sevtinge.hyperceiler.ui.fragment.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fan.common.base.BasePreferenceFragment;
import com.fan.common.base.FragmentTag;
import com.sevtinge.hyperceiler.utils.PrefsUtils;
import com.sevtinge.hyperceiler.utils.RestartHelper;

public abstract class SettingsPreferenceFragment extends BasePreferenceFragment {

    public String mTitle;
    public String mPreferenceKey;
    public int mContentResId = 0;
    public int mTitleResId = 0;
    public MenuItem mRestartMenu;
    private boolean mPreferenceHighlighted = false;
    private final String SAVE_HIGHLIGHTED_KEY = "android:preference_highlighted";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        highlightPreferenceIfNeeded(mPreferenceKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPreferenceHighlighted = savedInstanceState.getBoolean(SAVE_HIGHLIGHTED_KEY);
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        super.onCreatePreferences(bundle, s);
        Bundle args = getArguments();
        if (args != null) {
            mTitle = args.getString(":fragment:show_title");
            mTitleResId = args.getInt(":fragment:show_title_resid");
            mPreferenceKey = args.getString(":settings:fragment_args_key");
            mContentResId = args.getInt("contentResId");
        }
        if (mTitleResId != 0) setTitle(mTitleResId);
        if (!TextUtils.isEmpty(mTitle)) setTitle(mTitle);
        mContentResId = mContentResId != 0 ? mContentResId : getContentResId();
        if (mContentResId != 0) {
            setPreferencesFromResource(mContentResId, s);
            initPrefs();
        }
    }

    public void showRestartListener(int appLabel, String packageName) {
        if (packageName.equals("system")) {
            RestartHelper.showRestartSystemDialog(requireContext());
        } else {
            RestartHelper.showRestartDialog(requireContext(), getString(appLabel), packageName);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_HIGHLIGHTED_KEY, mPreferenceHighlighted);
    }

    public void highlightPreferenceIfNeeded(String key) {
        if (isAdded() && !mPreferenceHighlighted && !TextUtils.isEmpty(key)) {
            requestHighlight(key);
            mPreferenceHighlighted = true;
        }
    }

    public View.OnClickListener addRestartListener() {
        return null;
    }

    public SharedPreferences getSharedPreferences() {
        return PrefsUtils.mSharedPreferences;
    }

    public boolean hasKey(String key) {
        return getSharedPreferences().contains(key);
    }

    public void initPrefs() {
    }

    public abstract int getContentResId();

    private void analysisAnnocation(SettingsPreferenceFragment fragment) {
        Class<?> clazz = fragment.getClass();
        if (clazz.isAnnotationPresent(FragmentTag.class)) {
            FragmentTag tag = clazz.getAnnotation(FragmentTag.class);
            int resId = tag.resId();
            String pkg = tag.pkg();
            if (resId > 0) {
                fragment.showRestartListener(resId, pkg);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(com.fan.common.R.menu.menu_sub, menu);
        mRestartMenu = menu.findItem(com.fan.common.R.id.restart);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item == mRestartMenu) {
            analysisAnnocation(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
