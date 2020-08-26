package com.arsylk.xdiscord;

import android.content.Context;
import android.net.Uri;
import com.arsylk.xdiscord.Hooks.ModelEmojiUnicode;
import com.arsylk.xdiscord.Hooks.NetworkUtils;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.lang.reflect.Method;


public class XposedInit implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    public static final String DISCORD_PACKAGE = "com.discord";
    public static Class StoreEmojiCustom = null;
    private Method updateAvailableGuildEmojis = null;
    public static Class ModelEmojiCustom = null;
    private Method isAvailable = null, isUsable = null;
    public static Class MessageEntry = null, ModelMessage = null;
    private Method getContent = null;

    public static Class ModelEmojiUnicode = null;
    private Method getImageUri = null;

    public static Class NetworkUtils = null, NetworkUtils$downloadFile$1 = null;
    private Method downloadFile = null;


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
        // XposedBridge.hookMethod(updateAvailableGuildEmojis, new StoreEmojiCustom.UpdateAvailableGuildEmojisHook());
        // XposedBridge.hookMethod(isAvailable, new ModelEmojiCustom.IsAvailableHook());
        // XposedBridge.hookMethod(isUsable, new ModelEmojiCustom.IsUsableHook());
        // XposedBridge.hookMethod(getContent, new ModelMessage.GetContentHook());
        XposedBridge.hookMethod(getImageUri, new ModelEmojiUnicode.GetImageUriHook());
        XposedBridge.hookMethod(downloadFile, new NetworkUtils.DownloadFileHook());

        // hook constructors
        // XposedBridge.hookAllConstructors(MessageEntry, new MessageEntry.AllConstructorsHook());
    }

    private boolean findClasses(XC_LoadPackage.LoadPackageParam lpparam) {
        // StoreEmojiCustom = XposedHelpers.findClassIfExists("com.discord.stores.StoreEmojiCustom", lpparam.classLoader);
        // ModelEmojiCustom = XposedHelpers.findClassIfExists("com.discord.models.domain.emoji.ModelEmojiCustom", lpparam.classLoader);
        // MessageEntry = XposedHelpers.findClassIfExists("com.discord.widgets.chat.list.entries.MessageEntry", lpparam.classLoader);
        // ModelMessage = XposedHelpers.findClassIfExists("com.discord.models.domain.ModelMessage", lpparam.classLoader);
        ModelEmojiUnicode = XposedHelpers.findClassIfExists("com.discord.models.domain.emoji.ModelEmojiUnicode", lpparam.classLoader);
        NetworkUtils = XposedHelpers.findClassIfExists("com.discord.utilities.io.NetworkUtils", lpparam.classLoader);
        NetworkUtils$downloadFile$1 = XposedHelpers.findClassIfExists("com.discord.utilities.io.NetworkUtils$downloadFile$1", lpparam.classLoader);

        return (/* StoreEmojiCustom != null && ModelEmojiCustom != null && MessageEntry != null && ModelMessage != null && */
                ModelEmojiUnicode != null && NetworkUtils != null && NetworkUtils$downloadFile$1 != null);
    }

    private boolean findMethods(XC_LoadPackage.LoadPackageParam lpparam) {
        // updateAvailableGuildEmojis = XposedHelpers.findMethodExactIfExists(StoreEmojiCustom,
        //         "updateAvailableGuildEmojis",
        //         long.class, "com.discord.models.domain.ModelGuildMember", Collection.class);
        // isAvailable = XposedHelpers.findMethodExactIfExists(ModelEmojiCustom, "isAvailable");
        // isUsable = XposedHelpers.findMethodExactIfExists(ModelEmojiCustom, "isUsable");
        // getContent = XposedHelpers.findMethodExactIfExists(ModelMessage, "getContent");
        getImageUri = XposedHelpers.findMethodExactIfExists(ModelEmojiUnicode, "getImageUri", String.class, Context.class);
        downloadFile = XposedHelpers.findMethodExactIfExists(NetworkUtils, "downloadFile",
                Context.class, Uri.class, String.class, String.class,
                "kotlin.jvm.functions.Function1", "kotlin.jvm.functions.Function1");

        return (/* updateAvailableGuildEmojis != null && isAvailable != null && isUsable != null && getContent != null && */
                getImageUri != null && downloadFile != null);
    }
}
