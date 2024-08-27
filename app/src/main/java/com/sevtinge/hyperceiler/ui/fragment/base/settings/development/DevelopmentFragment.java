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
package com.sevtinge.hyperceiler.ui.fragment.base.settings.development;

import static com.sevtinge.hyperceiler.utils.shell.ShellUtils.safeExecCommandWithRoot;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.sevtinge.hyperceiler.R;
import com.sevtinge.hyperceiler.data.AppData;
import com.sevtinge.hyperceiler.module.base.dexkit.DexKit;
import com.sevtinge.hyperceiler.ui.fragment.base.SettingsPreferenceFragment;
import com.sevtinge.hyperceiler.utils.ContextUtils;
import com.sevtinge.hyperceiler.utils.DialogHelper;
import com.sevtinge.hyperceiler.utils.ThreadPoolManager;
import com.sevtinge.hyperceiler.utils.ToastHelper;
import com.sevtinge.hyperceiler.utils.shell.ShellInit;

import java.util.concurrent.ExecutorService;

import moralnorm.appcompat.app.AlertDialog;
import moralnorm.preference.Preference;

public class DevelopmentFragment extends SettingsPreferenceFragment implements Preference.OnPreferenceClickListener {

    Preference mCmdR;
    Preference mDeleteAllDexKitCache;

    public interface EditDialogCallback {
        void onInputReceived(String command);
    }

    @Override
    public int getContentResId() {
        return R.xml.prefs_development;
    }

    @Override
    public void initPrefs() {
        mCmdR = findPreference("prefs_key_development_cmd_r");
        mDeleteAllDexKitCache = findPreference("prefs_key_development_delete_all_dexkit_cache");
        mCmdR.setOnPreferenceClickListener(this);
        mDeleteAllDexKitCache.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case "prefs_key_development_cmd_r" -> {
                showInDialog(new DevelopmentKillFragment.EditDialogCallback() {
                    @Override
                    public void onInputReceived(String command) {
                        showOutDialog(safeExecCommandWithRoot(command));
                    }
                });
            }
            case "prefs_key_development_delete_all_dexkit_cache" -> {
                DialogHelper.showDialog(getActivity(), R.string.warn, R.string.delete_all_dexkit_cache_desc, (dialog, which) -> {
                    DexKit.deleteAllCache(getActivity());
                    Toast.makeText(getActivity(), R.string.delete_all_dexkit_cache_success, Toast.LENGTH_LONG).show();
                });
            }
        }
        return true;
    }

    private void showInDialog(DevelopmentKillFragment.EditDialogCallback callback) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_dialog, null);
        EditText input = view.findViewById(R.id.title);
        new AlertDialog.Builder(getActivity())
                .setTitle("# root@HyperCeiler > Input")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String userInput = input.getText().toString();
                    if (userInput.isEmpty()) {
                        dialog.dismiss();
                        showInDialog(callback);
                        return;
                    }
                    callback.onInputReceived(userInput);
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showOutDialog(String show) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle("# root@HyperCeiler > Output")
                .setMessage(show)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
