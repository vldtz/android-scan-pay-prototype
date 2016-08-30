package com.vladc.android.mobileerptool.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static boolean ifUp(Context ctx) {
        final ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
//                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
//            return true;
//        }
        final NetworkInfo info = conMgr.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (!info.isConnected() && !info.isConnectedOrConnecting()) {
            return false;
        }
        if (!info.isAvailable()) {
            return false;
        }
        return true;
    }
    
    public static boolean ifDown(Context ctx) {
        return !NetworkUtil.ifUp(ctx);
    }
}
