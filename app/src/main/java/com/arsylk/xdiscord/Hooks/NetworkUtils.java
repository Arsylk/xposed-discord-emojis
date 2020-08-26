package com.arsylk.xdiscord.Hooks;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import com.arsylk.xdiscord.XposedInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import java.io.File;
import java.util.Map;

public abstract class NetworkUtils {
    public static class DownloadFileHook extends XC_MethodReplacement {
        @Override
        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
            if(param.args != null && param.args.length > 0) {
                Context context = (Context) param.args[0];
                Uri uri = (Uri) param.args[1];
                String str = (String) param.args[2];
                String str2 = (String) param.args[3];
                Object function1 = param.args[4];
                Object function12 = param.args[5];


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(Html.fromHtml("<font color=\"#000000\">" + uri.getLastPathSegment() + "</font>"));
                builder.setNegativeButton("Cancel", null);
                builder.setItems(new String[]{"Downloads", "Pictures", "Style"}, (dialog, which) -> {
                    File directory = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
                    if(which == 1) directory = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
                    if(which == 2) directory = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES + "/Style");

                    String filename = uri.getLastPathSegment();
                    if(filename.endsWith(":large")) filename = filename.substring(0, filename.length() - 6);

                    File file = new File(directory, filename);

                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request notificationVisibility = new DownloadManager.Request(uri)
                            .setTitle(filename)
                            .setDescription(uri.toString())
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    DownloadManager.Request destinationInExternalPublicDir = notificationVisibility.setDestinationUri(Uri.fromFile(file));
                    destinationInExternalPublicDir.allowScanningByMediaScanner();

                    long j = downloadManager.enqueue(destinationInExternalPublicDir);
                    try {
                        Map<Long, Object> onDownloadListeners = (Map<Long, Object>) XposedHelpers.getStaticObjectField(XposedInit.NetworkUtils, "onDownloadListeners");
                        onDownloadListeners.put(j, XposedInit.NetworkUtils$downloadFile$1.getConstructors()[0].newInstance(j, downloadManager, function1, function12));
                    }catch (Exception e) {
                        XposedBridge.log(e);
                    }

                });
                builder.show();
            }

            return param.getResult();
        }
    }
}
