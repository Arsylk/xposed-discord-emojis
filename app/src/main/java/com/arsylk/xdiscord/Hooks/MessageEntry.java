package com.arsylk.xdiscord.Hooks;

import android.util.Pair;
import com.arsylk.xdiscord.XposedInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import java.util.HashMap;


public abstract class MessageEntry {
    public static class AllConstructorsHook extends XC_MethodHook {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            Object thisObject = param.thisObject;
            XposedHelpers.setBooleanField(thisObject, "animateEmojis", true);

            Object messageObject = XposedHelpers.getObjectField(thisObject, "message");
            String messageContent = (String) XposedHelpers.getObjectField(messageObject, "content");
            if(XposedHelpers.getAdditionalInstanceField(messageContent, "xHandled") == null) {
                HashMap allEmojis = (HashMap) XposedHelpers.getAdditionalStaticField(XposedInit.StoreEmojiCustom, "allEmojis");
                if(allEmojis != null) {
                    for(Object value : allEmojis.values()) {
                        Pair pair = (Pair) value;
                        String regexString = (String) pair.first, replaceString = (String) pair.second;

                        if(!messageContent.contains(replaceString))
                            messageContent = messageContent.replaceAll(regexString, (replaceString+" "));
                    }
                }

                XposedHelpers.setAdditionalInstanceField(messageContent, "xHandled", true);
                XposedHelpers.setObjectField(messageObject,"content", messageContent);
                XposedHelpers.setObjectField(thisObject, "message", messageObject);
            }
        }
    }
}
