package com.example.administrator.getuitest.demo.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Time：2020-03-10 on 11:37.
 * Decription:.
 * Author:jimlee.
 */
public class Config {
    public static final String AUTH_ACTION = "com.action.auth";
    private static final String TAG = Config.class.getSimpleName();
    public static String appid = "F3X4WEsJsY6Aj2rtPP9n9";
    public static String appkey = "YuPK31YsFr8cEaAjSEAku1";
    public static String appName = "个推Demo-839533925";
    public static String packageName = "com.getui.demo";
    public static String MASTERSECRET = "kINn82fIED9DFCyWJANKH";
    public static String SERVER_URL = "https://restapi.getui.com/";
    public static String authToken;

    public static void init(Context context) {
        parseManifests(context);
    }


    private static void parseManifests(Context context) {
        packageName = context.getPackageName();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            appName = appInfo.loadLabel(context.getPackageManager()).toString();
            if (appInfo.metaData != null) {
                appid = appInfo.metaData.getString("GETUI_APPID");
            }
        } catch (Exception e) {
            Log.i(TAG, "parse manifest failed = " + e);
        }
    }


}
