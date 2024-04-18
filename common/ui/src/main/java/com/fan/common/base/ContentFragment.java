package com.fan.common.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fan.common.R;

import fan.appcompat.app.Fragment;
import fan.core.utils.AttributeResolver;

public class ContentFragment extends Fragment {

    protected MenuItem mRestartMenu;

    @Override
    public View onInflateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    protected Fragment createFragment(androidx.fragment.app.Fragment fragment) {
        return null;
    }

    public void setTitle(int titleResId) {
        setTitle(getResources().getString(titleResId));
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            getActionBar().setTitle(title);
        }
    }

    public void setActionBarEndView(View view) {
        getActionBar().setEndView(view);
    }

    public void setActionBarEndIcon(@DrawableRes int resId, View.OnClickListener listener) {
        ImageView mRestartView = new ImageView(requireContext());
        mRestartView.setImageResource(resId);
        mRestartView.setOnClickListener(listener);
        setActionBarEndView(mRestartView);
    }

    public void setRestartView(View.OnClickListener listener) {
        if (listener != null) setActionBarEndIcon(AttributeResolver.resolve(requireContext(), fan.appcompat.R.attr.actionBarRefreshIcon), listener);
    }

}
