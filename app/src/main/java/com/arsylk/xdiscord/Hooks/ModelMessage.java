package com.arsylk.xdiscord.Hooks;

import android.util.Pair;
import com.arsylk.xdiscord.XposedInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import java.util.HashMap;

public abstract class ModelMessage {
    public static class GetContentHook extends XC_MethodReplacement {
        @Override
        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
            Object thisObject = param.thisObject;

            String content = (String) XposedHelpers.getObjectField(thisObject, "content");
            if(XposedHelpers.getAdditionalInstanceField(content, "xHandled") == null) {
                HashMap allEmojis = (HashMap) XposedHelpers.getAdditionalStaticField(XposedInit.StoreEmojiCustom, "allEmojis");
                if(allEmojis != null) {
                    for(Object value : allEmojis.values()) {
                        Pair pair = (Pair) value;
                        String regexString = (String) pair.first, replaceString = (String) pair.second;

                        content = content.replaceAll(regexString, replaceString);
                    }
                }

                XposedBridge.log(content);
                XposedHelpers.setAdditionalInstanceField(content, "xHandled", true);
                XposedHelpers.setObjectField(thisObject,"content", content);
            }

            return content;
        }
    }
}
