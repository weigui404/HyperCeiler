package com.sevtinge.hyperceiler.module.hook.systemui.plugin;

import static com.sevtinge.hyperceiler.module.base.tool.HookTool.hookMethod;
import static com.sevtinge.hyperceiler.utils.PropUtils.getProp;

import com.sevtinge.hyperceiler.module.base.dexkit.DexKit;
import com.sevtinge.hyperceiler.module.base.dexkit.IDexKit;
import com.sevtinge.hyperceiler.module.base.tool.HookTool;

import org.luckypray.dexkit.DexKitBridge;
import org.luckypray.dexkit.query.FindMethod;
import org.luckypray.dexkit.query.matchers.MethodMatcher;
import org.luckypray.dexkit.result.MethodData;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;

public class ShowDeviceName {

    static String deviceName = getProp("persist.sys.device_name");

    public static void initShowDeviceName(ClassLoader classLoader) {
        Method method = (Method) DexKit.getDexKitBridge("OnCarrierTextChanged", new IDexKit() {
            @Override
            public AnnotatedElement dexkit(DexKitBridge bridge) throws ReflectiveOperationException {
                MethodData methodData = bridge.findMethod(FindMethod.create()
                        .matcher(MethodMatcher.create()
                                .name("onCarrierTextChanged")
                        )).singleOrNull();
                return methodData.getMethodInstance(classLoader);
            }
        });
        hookMethod(method, new HookTool.MethodHook() {
            @Override
            protected void before(XC_MethodHook.MethodHookParam param) throws Throwable {
                param.args[0] = deviceName;
            }
        });
    }
}

