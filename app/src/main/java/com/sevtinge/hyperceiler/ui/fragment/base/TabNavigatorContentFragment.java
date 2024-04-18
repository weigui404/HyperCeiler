package com.sevtinge.hyperceiler.ui.fragment.base;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.sevtinge.hyperceiler.R;
import com.sevtinge.hyperceiler.data.adapter.ModSearchAdapter;
import com.sevtinge.hyperceiler.data.adapter.NavigationPagerAdapter;
import com.sevtinge.hyperceiler.ui.SubSettings;
import com.sevtinge.hyperceiler.ui.fragment.base.navigation.AboutPageFragment;
import com.sevtinge.hyperceiler.ui.fragment.base.navigation.HomePageFragment;
import com.sevtinge.hyperceiler.ui.fragment.base.navigation.SettingsPageFragment;
import com.sevtinge.hyperceiler.utils.DialogHelper;
import com.sevtinge.hyperceiler.utils.Helpers;
import com.sevtinge.hyperceiler.utils.SearchModeHelper;
import com.fan.common.utils.SettingLauncherHelper;
import com.sevtinge.hyperceiler.utils.blur.MiBlurUtils;

import java.util.ArrayList;
import java.util.List;

import fan.appcompat.app.AppCompatActivity;
import fan.view.SearchActionMode;

public class TabNavigatorContentFragment extends Fragment {

    String lastFilter;
    View mContainer;
    View mSearchView;
    TextView mSearchInputView;
    RecyclerView mSearchResultView;
    ModSearchAdapter mSearchAdapter;
    TextWatcher mSearchResultListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            findMod(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            findMod(s.toString());
        }
    };
    ModSearchAdapter.onItemClickListener onSearchItemClickListener = (view, ad) -> {
        Bundle args = new Bundle();
        args.putString(":settings:fragment_args_key", ad.key);
        SettingLauncherHelper.onStartSettingsForArguments(
            getContext(),
            SubSettings.class,
            ad.fragment,
            args,
            ad.catTitleResId
        );
    };


    RadioGroup mNavigationView;
    ViewPager mFragmentPage;
    HomePageFragment mHomePage = new HomePageFragment();
    SettingsPageFragment mSettingsPage = new SettingsPageFragment();
    AboutPageFragment mAboutPage = new AboutPageFragment();
    NavigationPagerAdapter mNavigationPagerAdapter;
    List<androidx.fragment.app.Fragment> mFragmentList = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //setThemeRes(R.style.TabNavigatorContentFragmentTheme);
    }

    public int getBottomTabMenu() {
        return R.menu.menu_navigation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.activity_navigation, container, false);
        return mContainer;
    }

    /*@Override
    public View onInflateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.activity_navigation, container, false);
        return mContainer;
    }*/

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initSearchView(view);
        initNavigationView(view);
    }

    private void initSearchView(View view) {
        mSearchView = view.findViewById(R.id.search_view);
        mSearchInputView = view.findViewById(android.R.id.input);
        mSearchResultView = view.findViewById(R.id.search_result_view);
        mSearchAdapter = new ModSearchAdapter();
        mSearchInputView.setHint(getResources().getString(R.string.search));
        mSearchResultView.setAdapter(mSearchAdapter);

        mSearchView.setOnClickListener(v -> startSearchMode());
        mSearchAdapter.setOnItemClickListener(onSearchItemClickListener);
    }

    private void initNavigationView(View view) {
        mFragmentPage = view.findViewById(R.id.frame_page);
        mNavigationView = view.findViewById(R.id.navigation);
        mFragmentList.add(mHomePage);
        mFragmentList.add(mSettingsPage);
        mFragmentList.add(mAboutPage);
        mNavigationPagerAdapter = new NavigationPagerAdapter(getParentFragmentManager(), mFragmentList);
        mFragmentPage.setAdapter(mNavigationPagerAdapter);

        updateNavigatorInfo(mFragmentPage.getCurrentItem());

        int blurRadius = Helpers.isDarkMode(requireContext()) ? 140 : 180;
        int alpha = Helpers.isDarkMode(requireContext()) ? 80 : 100;
        MiBlurUtils.setContainerPassBlur(mNavigationView, blurRadius);
        MiBlurUtils.setMiViewBlurMode(mNavigationView, 3);
        MiBlurUtils.clearMiBackgroundBlendColor(mNavigationView);
        MiBlurUtils.addMiBackgroundBlendColor(mNavigationView, Color.argb(alpha, 0, 0, 0), 103);
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
        mNavigationView.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.navigation_home) {
                changeSelect(0);
            } else if (checkedId == R.id.navigation_settings) {
                changeSelect(1);
            } else if (checkedId == R.id.navigation_about) {
                changeSelect(2);
            }
        });
    }

    private SearchActionMode startSearchMode() {
        return SearchModeHelper.startSearchMode(
            (AppCompatActivity) getActivity(),
            mSearchResultView,
            mFragmentPage,
            mSearchView,
            mSearchView,
            mSearchResultListener
        );
    }

    private void changeSelect(int position) {
        mFragmentPage.setCurrentItem(position);
        updateNavigatorInfo(position);
    }

    protected void updateNavigatorInfo(int position) {
        switch (position) {
            case 0 -> {
                setTitle("首页");
            }
            case 1 -> {
                setTitle("设置");
            }
            case 2 -> {
                setTitle("关于");
            }
        }
    }

    protected void setTitle(int titleResId) {
        setTitle(getResources().getString(titleResId));
    }

    protected void setTitle(CharSequence title) {
        getActivity().setTitle(title);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem item = menu.add(R.id.miuix_action_end_menu_group, 1, 0, R.string.soft_reboot);
        item.setIcon(R.drawable.ic_reboot_small);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
        } else if (itemId == 1) {
            DialogHelper.showRestartDialog(requireContext());
        }
        return super.onOptionsItemSelected(item);
    }

    void findMod(String filter) {
        lastFilter = filter;
        mSearchResultView.setVisibility(filter.equals("") ? View.GONE : View.VISIBLE);
        ModSearchAdapter adapter = (ModSearchAdapter) mSearchResultView.getAdapter();
        if (adapter == null) return;
        adapter.getFilter().filter(filter);
    }
}
