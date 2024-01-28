package com.sevtinge.hyperceiler.ui.fragment.navigation;

import static com.sevtinge.hyperceiler.utils.Helpers.isDarkMode;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.sevtinge.hyperceiler.R;
import com.sevtinge.hyperceiler.data.ModData;
import com.sevtinge.hyperceiler.data.adapter.ModSearchAdapter;
import com.sevtinge.hyperceiler.data.adapter.NavigationPagerAdapter;
import com.sevtinge.hyperceiler.ui.SubSettings;
import com.sevtinge.hyperceiler.utils.SettingLauncherHelper;
import com.sevtinge.hyperceiler.utils.blur.MiBlurUtils;

import java.util.ArrayList;

import fan.appcompat.app.Fragment;
import fan.view.SearchActionMode;

public class NavigationFragment extends Fragment {

    String lastFilter;
    View mSearchView;
    TextView mSearchInputView;
    RecyclerView mSearchResultView;
    ModSearchAdapter mSearchAdapter;

    View mContainer;
    ViewPager mFragmentPage;
    RadioGroup mNavigationView;
    RadioButton mHomeNav, mSettingsNav, mAboutNav;
    ArrayList<androidx.fragment.app.Fragment> mFragmentList = new ArrayList<>();
    NavigationPagerAdapter mNavigationPagerAdapter;


    @Override
    public View onInflateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.activity_navigation, container, false);
        initView();
        return mContainer;
    }

    private void initView() {
        /*initSearchView();*/
        initNavigationView();
    }

    private void initSearchView() {
        mSearchView = mContainer.findViewById(R.id.search_view);
        mSearchInputView = mContainer.findViewById(android.R.id.input);
        mSearchResultView = mContainer.findViewById(R.id.search_result_view);
        mSearchAdapter = new ModSearchAdapter();
        mSearchInputView.setHint(getResources().getString(R.string.search));
        mSearchResultView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchResultView.setAdapter(mSearchAdapter);

        mSearchView.setOnClickListener(v -> startSearchMode());
        mSearchAdapter.setOnItemClickListener((view, ad) -> onSearchItemClickListener(ad));
    }

    private void initNavigationView() {
        mFragmentPage = mContainer.findViewById(R.id.frame_page);
        mNavigationView = mContainer.findViewById(R.id.navigation);
        mHomeNav = mContainer.findViewById(R.id.navigation_home);
        mSettingsNav = mContainer.findViewById(R.id.navigation_settings);
        mAboutNav = mContainer.findViewById(R.id.navigation_about);


        mNavigationPagerAdapter = new NavigationPagerAdapter(getChildFragmentManager(), mFragmentList);
        mFragmentPage.setAdapter(mNavigationPagerAdapter);


        int i = isDarkMode(getContext()) ? 140 : 100;
        int a = isDarkMode(getContext()) ? 80 : 100;
        MiBlurUtils.setContainerPassBlur(mNavigationView, i);
        MiBlurUtils.setMiViewBlurMode(mNavigationView, 3);
        MiBlurUtils.clearMiBackgroundBlendColor(mNavigationView);
        MiBlurUtils.addMiBackgroundBlendColor(mNavigationView, Color.argb(a, 0, 0, 0), 103);

        mNavigationView.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.navigation_home) {
                mFragmentPage.setCurrentItem(0);
            } else if (checkedId == R.id.navigation_settings) {
                mFragmentPage.setCurrentItem(1);
            } else if (checkedId == R.id.navigation_about) {
                mFragmentPage.setCurrentItem(2);
            }
        });

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
        switch (position) {
            case 0 -> {
                mHomeNav.setChecked(true);
                mFragmentPage.setCurrentItem(0);
                /*setTitle(R.string.app_name);*/
            }
            case 1 -> {
                mSettingsNav.setChecked(true);
                mFragmentPage.setCurrentItem(1);
                /*setTitle(R.string.settings);*/
            }
            case 2 -> {
                mAboutNav.setChecked(true);
                mFragmentPage.setCurrentItem(2);
                /*setTitle(R.string.about);*/
            }
        }
    }

    private void onSearchItemClickListener(ModData ad) {
        Bundle args = new Bundle();
        args.putString(":settings:fragment_args_key", ad.key);
        SettingLauncherHelper.onStartSettingsForArguments(
            getContext(),
            SubSettings.class,
            ad.fragment,
            args,
            ad.catTitleResId
        );
    }

    private SearchActionMode startSearchMode() {
        return null;
        /*return SearchModeHelper.startSearchMode(
                (AppCompatActivity) getActivity(),
            mSearchResultView,
            mFragmentPage,
            mSearchView,
            mContainer.findViewById(android.R.id.list_container),
            mSearchResultListener
        );*/
    }

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

    void findMod(String filter) {
        lastFilter = filter;
        mSearchResultView.setVisibility(filter.equals("") ? View.GONE : View.VISIBLE);
        ModSearchAdapter adapter = (ModSearchAdapter) mSearchResultView.getAdapter();
        if (adapter == null) return;
        adapter.getFilter().filter(filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
