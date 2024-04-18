package com.sevtinge.hyperceiler.ui.fragment.base.navigation;

import static com.sevtinge.hyperceiler.utils.api.VoyagerApisKt.isPad;
import static com.sevtinge.hyperceiler.utils.devicesdk.SystemSDKKt.getBaseOs;
import static com.sevtinge.hyperceiler.utils.devicesdk.SystemSDKKt.getRomAuthor;
import static com.sevtinge.hyperceiler.utils.devicesdk.SystemSDKKt.isAndroidVersion;
import static com.sevtinge.hyperceiler.utils.devicesdk.SystemSDKKt.isMoreHyperOSVersion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.sevtinge.hyperceiler.R;
import com.sevtinge.hyperceiler.prefs.TipsPreference;
import com.sevtinge.hyperceiler.ui.MainActivityContextHelper;
import com.sevtinge.hyperceiler.ui.fragment.base.NavigatorFragment;
import com.sevtinge.hyperceiler.utils.devicesdk.DeviceSDKKt;
import com.sevtinge.hyperceiler.utils.devicesdk.SystemSDKKt;

import java.util.Objects;

import fan.preference.Preference;

public class HomePageFragment extends NavigatorFragment {

    Preference mPowerSetting;
    Preference mMTB;
    Preference mSecurityCenter;
    Preference mMiLink;
    Preference mAod;
    Preference mGuardProvider;
    Preference mMirror;
    Preference mHeadtipWarn;

    TipsPreference mTips;
    MainActivityContextHelper mainActivityContextHelper;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                mTips.updateTips();//在这里写需要刷新完成的代码
                removeMessages(0x11);
                sendEmptyMessageDelayed(0x11, 6000);//这里想几秒刷新一次就写几秒*/
            }
        }
    };

    @Override
    public int getContentResId() {
        return R.xml.prefs_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getActionBar().hide();*/
        Message message = mHandler.obtainMessage(0x11);
        mHandler.sendMessageDelayed(message, 6000);
    }

    @Override
    public void initPrefs() {
        mPowerSetting = findPreference("prefs_key_powerkeeper");
        mMTB = findPreference("prefs_key_mtb");
        mSecurityCenter = findPreference("prefs_key_security_center");
        mMiLink = findPreference("prefs_key_milink");
        mAod = findPreference("prefs_key_aod");
        mGuardProvider = findPreference("prefs_key_guardprovider");
        mMirror = findPreference("prefs_key_mirror");
        mHeadtipWarn = findPreference("prefs_key_headtip_warn");
        mTips = findPreference("prefs_key_tips");

        mPowerSetting.setVisible(!isAndroidVersion(30));
        mMTB.setVisible(!isAndroidVersion(30));

        if (isMoreHyperOSVersion(1f)) {
            mAod.setTitle(R.string.aod_hyperos);
            mMiLink.setTitle(R.string.milink_hyperos);
            mGuardProvider.setTitle(R.string.guard_provider_hyperos);
            mMirror.setTitle(R.string.mirror_hyperos);
            mSecurityCenter.setTitle(R.string.security_center_hyperos);
        } else {
            mAod.setTitle(R.string.aod);
            mMiLink.setTitle(R.string.milink);
            mGuardProvider.setTitle(R.string.guard_provider);
            mMirror.setTitle(R.string.mirror);
            if (isPad()) {
                mSecurityCenter.setTitle(R.string.security_center_pad);
            } else {
                mSecurityCenter.setTitle(R.string.security_center);
            }
        }

        mainActivityContextHelper = new MainActivityContextHelper(requireContext());

        isOfficialRom();
        if(!getIsOfficialRom()) isSignPass();
    }

    public void isOfficialRom() {
        mHeadtipWarn.setTitle(R.string.headtip_warn_not_offical_rom);
        mHeadtipWarn.setVisible(getIsOfficialRom());
    }

    public boolean getIsOfficialRom() {
        return (!(DeviceSDKKt.getFingerPrint().equals(getBaseOs()) || com.sevtinge.hyperceiler.utils.devicesdk.SystemSDKKt.getBaseOs().startsWith("V")) && !com.sevtinge.hyperceiler.utils.devicesdk.SystemSDKKt.getBaseOs().isEmpty()) || !getRomAuthor().isEmpty() || Objects.equals(SystemSDKKt.getHost(), "xiaomi.eu") || !SystemSDKKt.getHost().startsWith("pangu-build-component-system");
    }

    public void isSignPass() {
        mHeadtipWarn.setTitle(R.string.headtip_warn_sign_verification_failed);
        mHeadtipWarn.setVisible(!mainActivityContextHelper.isSignCheckPass());
    }
}
