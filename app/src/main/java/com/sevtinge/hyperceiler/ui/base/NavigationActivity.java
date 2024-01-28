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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.sevtinge.hyperceiler.R;
import com.sevtinge.hyperceiler.data.adapter.NavigationPagerAdapter;
import com.sevtinge.hyperceiler.ui.SubSettings;
import com.sevtinge.hyperceiler.ui.fragment.navigation.AboutPageFragment;
import com.sevtinge.hyperceiler.ui.fragment.navigation.HomePageFragment;
import com.sevtinge.hyperceiler.ui.fragment.navigation.SettingsPageFragment;

import java.util.ArrayList;
import java.util.List;

import fan.preference.Preference;
import fan.preference.PreferenceFragmentCompat;
import fan.preference.internal.compat.PreferenceFragmentCompat2;

public abstract class NavigationActivity extends BaseActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    ViewPager mFragmentPage;
    NavigationPagerAdapter mNavigationPagerAdapter;
    List<Fragment> mFragmentList = new ArrayList<>();
    HomePageFragment mHomePage = new HomePageFragment();
    SettingsPageFragment mSettingsPage = new SettingsPageFragment();
    AboutPageFragment mAboutPage = new AboutPageFragment();

    public abstract int getBottomTabMenu();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initNavigationView();
        /*setRestartView(view -> DialogHelper.showRestartDialog(this));*/
    }

    private void initNavigationView() {
        mFragmentPage = findViewById(R.id.frame_page);
        mFragmentList.add(mHomePage);
        mFragmentList.add(mSettingsPage);
        mFragmentList.add(mAboutPage);
        mNavigationPagerAdapter = new NavigationPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mFragmentPage.setAdapter(mNavigationPagerAdapter);


        /*int i;
        if (isDarkMode(this)) i = 140; else i = 180;
        int a;
        if (isDarkMode(this)) a = 80; else a = 100;
        MiBlurUtils.setContainerPassBlur(mNavigationView, i);
        MiBlurUtils.setMiViewBlurMode(mNavigationView, 3);
        MiBlurUtils.clearMiBackgroundBlendColor(mNavigationView);
        MiBlurUtils.addMiBackgroundBlendColor(mNavigationView, Color.argb(a, 0, 0, 0), 103);*/

        mFragmentPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeSelect(position);
                /*mSearchView.setVisibility(position == 0 ? View.VISIBLE : View.GONE);*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeSelect(int position) {
        mFragmentPage.setCurrentItem(position);
    }

    @Override
    public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat2 preferenceFragmentCompat2, @NonNull Preference preference) {
        mProxy.onStartSettingsForArguments(SubSettings.class, preference, false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getBottomTabMenu(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
            changeSelect(0);
        } else if (itemId == R.id.navigation_settings) {
            changeSelect(1);
        } else if (itemId == R.id.navigation_about) {
            changeSelect(2);
        }
        return super.onOptionsItemSelected(item);
    }
}
