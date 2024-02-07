package com.sevtinge.hyperceiler.ui.fragment.base;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fan.preference.PreferenceFragment;

public abstract class NavigatorFragment extends PreferenceFragment {

    protected abstract int getContentResId();

    @Override
    public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String s) {
        FragmentProxy mProxy = new FragmentProxy(this);
        mProxy.onCreatePreferences(getPreferenceManager());
        if (getContentResId() != 0) {
            setPreferencesFromResource(getContentResId(), s);
            initPrefs();
        }
    }

    protected void initPrefs() {}

    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public boolean isInEditActionMode() {
        return false;
    }

    @Override
    public boolean isIsInSearchActionMode() {
        return false;
    }

    @Override
    public boolean isRegisterResponsive() {
        return false;
    }

    @Override
    public void onActionModeFinished(ActionMode actionMode) {

    }

    @Override
    public void onActionModeStarted(ActionMode actionMode) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onCreatePanelMenu(int i, Menu menu) {
        return false;
    }

    @Override
    public View onInflateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onPanelClosed(int i, Menu menu) {

    }

    @Override
    public void onPreparePanel(int i, View view, Menu menu) {

    }

    @Override
    public void onViewInflated(View view, Bundle bundle) {

    }

    @Override
    public void setThemeRes(int i) {

    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void onExtraPaddingChanged(int i) {

    }

    @Override
    public void onDispatchNestedScrollOffset(int[] ints) {

    }

    @Override
    public void onProcessBindViewWithContentInset(Rect rect) {

    }

    @Override
    public void setCorrectNestedScrollMotionEventEnabled(boolean b) {

    }

    @Override
    public void dismissImmersionMenu(boolean b) {

    }

    @Override
    public void setImmersionMenuEnabled(boolean b) {

    }

    @Override
    public void showImmersionMenu() {

    }

    @Override
    public void showImmersionMenu(View view, ViewGroup viewGroup) {

    }
}
