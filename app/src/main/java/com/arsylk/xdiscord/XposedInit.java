package com.arsylk.xdiscord;

import com.arsylk.xdiscord.Hooks.MessageEntry;
import com.arsylk.xdiscord.Hooks.ModelEmojiCustom;
import com.arsylk.xdiscord.Hooks.ModelMessage;
import com.arsylk.xdiscord.Hooks.StoreEmojiCustom;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.lang.reflect.Method;
import java.util.Collection;


public class XposedInit implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    public static final String DISCORD_PACKAGE = "com.discord";

    public static Class StoreEmojiCustom = null;
    private Method updateAvailableGuildEmojis = null;
    public static Class ModelEmojiCustom = null;
    private Method isAvailable = null, isUsable = null;
    public static Class MessageEntry = null, ModelMessage = null;
    private Method getContent = null;


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(!lpparam.packageName.equals(DISCORD_PACKAGE))
            return;
        XposedBridge.log("Loaded app: " + lpparam.packageName);


        if(!findClasses(lpparam)) {
            XposedBridge.log("Failed to find necessary classes");
            return;
        }
        XposedBridge.log("All necessary classes found");

        if(!findMethods(lpparam)) {
            XposedBridge.log("Failed to find necessary methods");
            return;
        }
        XposedBridge.log("All necessary methods found");


        // hook methods
        XposedBridge.hookMethod(updateAvailableGuildEmojis, new StoreEmojiCustom.UpdateAvailableGuildEmojisHook());
        XposedBridge.hookMethod(isAvailable, new ModelEmojiCustom.IsAvailableHook());
        XposedBridge.hookMethod(isUsable, new ModelEmojiCustom.IsUsableHook());
        // XposedBridge.hookMethod(getContent, new ModelMessage.GetContentHook());

        // hook constructors
        XposedBridge.hookAllConstructors(MessageEntry, new MessageEntry.AllConstructorsHook());
    }

    private boolean findClasses(XC_LoadPackage.LoadPackageParam lpparam) {
        StoreEmojiCustom = XposedHelpers.findClassIfExists("com.discord.stores.StoreEmojiCustom", lpparam.classLoader);
        ModelEmojiCustom = XposedHelpers.findClassIfExists("com.discord.models.domain.emoji.ModelEmojiCustom", lpparam.classLoader);
        MessageEntry = XposedHelpers.findClassIfExists("com.discord.widgets.chat.list.entries.MessageEntry", lpparam.classLoader);
        ModelMessage = XposedHelpers.findClassIfExists("com.discord.models.domain.ModelMessage", lpparam.classLoader);

        return (StoreEmojiCustom != null && ModelEmojiCustom != null && MessageEntry != null && ModelMessage != null);
    }

    private boolean findMethods(XC_LoadPackage.LoadPackageParam lpparam) {
        updateAvailableGuildEmojis = XposedHelpers.findMethodExactIfExists(StoreEmojiCustom,
                "updateAvailableGuildEmojis",
                long.class, "com.discord.models.domain.ModelGuildMember", Collection.class);
        isAvailable = XposedHelpers.findMethodExactIfExists(ModelEmojiCustom, "isAvailable");
        isUsable = XposedHelpers.findMethodExactIfExists(ModelEmojiCustom, "isUsable");
        getContent = XposedHelpers.findMethodExactIfExists(ModelMessage, "getContent");

        return (updateAvailableGuildEmojis != null && isAvailable != null && isUsable != null && getContent != null);
    }
}
