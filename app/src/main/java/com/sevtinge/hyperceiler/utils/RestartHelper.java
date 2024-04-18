package com.sevtinge.hyperceiler.utils;

import android.content.Context;

import com.sevtinge.hyperceiler.R;
import com.sevtinge.hyperceiler.utils.log.AndroidLogUtils;

import fan.appcompat.app.AlertDialog;

public class RestartHelper {

    public static void showRestartSystemDialog(Context context) {
        showRestartDialog(context, true, "", new String[]{""});
    }

    public static void showRestartDialog(Context context, String appLabel, String packageName) {
        showRestartDialog(context, false, appLabel, new String[]{packageName});
    }

    public static void showRestartDialog(Context context, String appLabel, String[] packageName) {
        showRestartDialog(context, false, appLabel, packageName);
    }

    public static void showRestartDialog(Context context, boolean isRestartSystem, String appLabel, String[] packageName) {
        String isSystem = context.getResources().getString(R.string.restart_app_desc, appLabel);
        String isOther = context.getResources().getString(R.string.restart_app_desc, " " + appLabel + " ");

        new AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(context.getResources().getString(R.string.soft_reboot) + " " + appLabel)
            .setMessage(isRestartSystem ? isSystem : isOther)
            .setHapticFeedbackEnabled(true)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> doRestart(context, packageName, isRestartSystem))
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    private static void doRestart(Context context, String[] packageName, boolean isRestartSystem) {
        boolean result = false;
        boolean pid = true;
        if (isRestartSystem) {
            result = ShellUtils.getResultBoolean("reboot", true);
        } else {
            if (packageName != null) {
                for (String packageGet : packageName) {
                    if (packageGet == null) {
                        continue;
                    }
                    // String test = "XX";
                    // ShellUtils.CommandResult commandResult = ShellUtils.execCommand("{ [[ $(pgrep -f '" + packageGet +
                    //     "' | grep -v $$) != \"\" ]] && { pkill -l 9 -f \"" + packageGet +
                    //     "\"; }; } || { echo \"kill error\"; }", true, true);
                    ShellUtils.CommandResult commandResult =
                        ShellUtils.execCommand("{ pid=$(pgrep -f '" + packageGet + "' | grep -v $$);" +
                                " [[ $pid != \"\" ]] && { pkill -l 9 -f \"" + packageGet + "\";" +
                                " { [[ $? != 0 ]] && { killall -s 9 \"" + packageGet + "\" &>/dev/null;};}" +
                                " || { { for i in $pid; do kill -s 9 \"$i\" &>/dev/null;done;};}" +
                                " || { echo \"kill error\";};};}" +
                                " || { echo \"kill error\";}",
                            true, true);
                    if (commandResult.result == 0) {
                        if (commandResult.successMsg.equals("kill error")) {
                            pid = false;
                        } else result = true;
                    } else
                        AndroidLogUtils.LogE("doRestart: ", "result: " + commandResult.result +
                            " errorMsg: " + commandResult.errorMsg + " package: " + packageGet, null);
                }
            } else {
                AndroidLogUtils.LogE("doRestart: ", "packageName is null", null);
            }
            // result = ShellUtils.getResultBoolean("pkill -l 9 -f " + packageName, true);
        }
        if (!result) {
            new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.tip)
                .setMessage(isRestartSystem ? R.string.reboot_failed :
                    pid ? R.string.kill_failed : R.string.pid_failed)
                .setHapticFeedbackEnabled(true)
                .setPositiveButton(android.R.string.ok, null)
                .show();
        }
    }
}
