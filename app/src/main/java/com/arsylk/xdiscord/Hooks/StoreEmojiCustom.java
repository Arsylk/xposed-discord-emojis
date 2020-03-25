package com.arsylk.xdiscord.Hooks;

import android.util.Pair;
import com.arsylk.xdiscord.XposedInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import java.util.HashMap;
import java.util.Map;

public abstract class StoreEmojiCustom {
    public static class UpdateAvailableGuildEmojisHook extends XC_MethodHook {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            Object thisObject = param.thisObject;

            HashMap<String, Pair<String, String>> allEmojis = new HashMap<>();
            HashMap allGuildEmojis = (HashMap) XposedHelpers.getObjectField(thisObject, "allGuildEmojis");
            if(allGuildEmojis != null) {
                for(Object allGuildEmojisValue : allGuildEmojis.values()) {
                    if(allGuildEmojisValue != null) {
                        for(Object emoji : ((Map) allGuildEmojisValue).values()) {
                            String emojiIdStr = (String) XposedHelpers.callMethod(emoji, "getIdStr");
                            String emojiRegex = (String) XposedHelpers.callMethod(emoji, "getColonRegex");
                            String emojiReplace = (String) XposedHelpers.callMethod(emoji, "getMessageContentReplacement");

                            allEmojis.put(emojiIdStr, new Pair<>(emojiRegex, emojiReplace));
                        }
                    }
                }
            }



            XposedHelpers.setAdditionalStaticField(XposedInit.StoreEmojiCustom, "allEmojis", allEmojis);
        }
    }
}
