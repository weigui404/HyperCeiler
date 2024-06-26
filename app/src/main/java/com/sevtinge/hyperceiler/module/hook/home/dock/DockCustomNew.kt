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
package com.sevtinge.hyperceiler.module.hook.home.dock

import android.app.AndroidAppHelper
import android.graphics.*
import android.view.Gravity
import android.widget.FrameLayout
import com.github.kyuubiran.ezxhelper.EzXHelper.appContext
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHooks
import com.sevtinge.hyperceiler.module.base.BaseHook
import com.sevtinge.hyperceiler.utils.api.dp2px
import com.sevtinge.hyperceiler.utils.blur.MiBlurUtilsKt.addMiBackgroundBlendColor
import com.sevtinge.hyperceiler.utils.blur.MiBlurUtilsKt.setBlurRoundRect
import com.sevtinge.hyperceiler.utils.blur.MiBlurUtilsKt.setMiViewBlurMode
import com.sevtinge.hyperceiler.utils.findClass
import com.sevtinge.hyperceiler.utils.getObjectField
import com.sevtinge.hyperceiler.utils.hookAfterMethod
import de.robv.android.xposed.XposedHelpers

object DockCustomNew : BaseHook() {
    override fun init() {
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()

        launcherClass.constructors.toList().createHooks {
            after {
                val context = AndroidAppHelper.currentApplication().applicationContext
                var mDockBlur = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDockBlur")
                if (mDockBlur != null) return@after
                mDockBlur = FrameLayout(context)
                XposedHelpers.setAdditionalInstanceField(it.thisObject, "mDockBlur", mDockBlur)
            }
        }

        launcherClass.hookAfterMethod("setupViews") {
            val mHotSeats = it.thisObject.getObjectField("mHotSeats") as FrameLayout
            val mDockBlur =
                XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDockBlur") as FrameLayout
            val mDockRadius =
                dp2px(appContext, mPrefsMap.getInt("home_dock_bg_radius", 30).toFloat())
            val mDockHeight =
                dp2px(appContext, mPrefsMap.getInt("home_dock_bg_height", 80).toFloat())
            val mDockMargin = dp2px(
                appContext, (mPrefsMap.getInt("home_dock_bg_margin_horizontal", 30) - 6).toFloat()
            )
            val mDockBottomMargin = dp2px(
                appContext, (mPrefsMap.getInt("home_dock_bg_margin_bottom", 30) - 92).toFloat()
            )
            if (mPrefsMap.getStringAsInt("home_dock_add_blur", 0) == 1) {
                mDockBlur.setMiViewBlurMode(1)
            }
            mDockBlur.setBlurRoundRect(mDockRadius)
            mDockBlur.setBackgroundColor(mPrefsMap.getInt("home_dock_bg_color", -1))
            mDockBlur.layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mDockHeight)
                    .also { layoutParams ->
                        layoutParams.gravity = Gravity.BOTTOM
                        layoutParams.setMargins(mDockMargin, 0, mDockMargin, mDockBottomMargin)
                    }
            mHotSeats.addView(mDockBlur, 0)
        }

    }
}
