package com.arsylk.xdiscord.Hooks;

import de.robv.android.xposed.XC_MethodHook;

public abstract class ModelEmojiUnicode {
    public static class GetImageUriHook extends XC_MethodHook {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if(param.args != null && param.args.length > 0) {
                if(String.valueOf(param.args[0]) == "1f52b" || String.valueOf(param.args[0]).equals("1f52b")) {
                    param.setResult("https://puu.sh/ENY1h/5b2f809def.png");
                }
            }
        }
    }
}
