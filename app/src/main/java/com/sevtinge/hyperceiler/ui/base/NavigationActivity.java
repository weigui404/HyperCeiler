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
package com.sevtinge.hyperceiler.ui.base;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.sevtinge.hyperceiler.R;
import com.sevtinge.hyperceiler.ui.SubSettings;
import com.sevtinge.hyperceiler.ui.fragment.base.TabNavigatorContentFragment;

import fan.external.view.weatherview.HolidayTheme;
import fan.external.view.weatherview.HolidayThemeHelper;
import fan.preference.Preference;
import fan.preference.PreferenceFragment;
import fan.preference.internal.compat.PreferenceFragmentCompat;

public abstract class NavigationActivity extends SettingsActivity implements PreferenceFragment.OnPreferenceStartFragmentCallback {

    public abstract int getBottomTabMenu();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragment(new TabNavigatorContentFragment());
        /*setRestartView(view -> DialogHelper.showRestartDialog(this));*/
    }

    @Override
    public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat preferenceFragmentCompat, @NonNull Preference preference) {
        mProxy.onStartSettingsForArguments(SubSettings.class, preference, false);
        return true;
    }

    @Override
    protected void onResume() {
        HolidayThemeHelper.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        HolidayThemeHelper.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        HolidayThemeHelper.onDestroy();
        super.onDestroy();
    }
}
