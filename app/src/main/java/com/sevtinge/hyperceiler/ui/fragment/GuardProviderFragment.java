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
package com.sevtinge.hyperceiler.ui.fragment;

import static com.sevtinge.hyperceiler.utils.devicesdk.SystemSDKKt.isMoreHyperOSVersion;

import android.view.View;

import com.sevtinge.hyperceiler.R;
import com.fan.common.base.BaseSettingsActivity;
import com.sevtinge.hyperceiler.ui.fragment.base.SettingsPreferenceFragment;
import com.sevtinge.hyperceiler.utils.RestartHelper;

public class GuardProviderFragment extends SettingsPreferenceFragment {
    @Override
    public int getContentResId() {
        return R.xml.guard_provider;
    }

    @Override
    public View.OnClickListener addRestartListener() {
        return view -> RestartHelper.showRestartDialog(requireContext(),
            getResources().getString(!isMoreHyperOSVersion(1f) ? R.string.guard_provider : R.string.guard_provider_hyperos),
            "com.miui.guardprovider"
        );
    }
}
