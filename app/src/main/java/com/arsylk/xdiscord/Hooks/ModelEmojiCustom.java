package com.arsylk.xdiscord.Hooks;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;


public abstract class ModelEmojiCustom {
    public static class IsAvailableHook extends XC_MethodReplacement {
        @Override
        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
            Object thisObject = param.thisObject;
            XposedHelpers.setObjectField(thisObject, "available", Boolean.TRUE);

            return Boolean.TRUE;
        }
    }
    public static class IsUsableHook extends XC_MethodReplacement {
        @Override
        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
            Object thisObject = param.thisObject;
            XposedHelpers.setObjectField(thisObject, "isUsable", Boolean.TRUE);

            return Boolean.TRUE;
        }
    }
}
