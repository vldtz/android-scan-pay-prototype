package com.vladc.android.mobileerptool;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.vladc.android.mobileerptool.data.MobileErpDbHelper;

import java.io.File;

/**
 * @author VladA xx
 * $Id: MobileERPApplication.java 8369 2014-02-28 15:52:24Z vlada $
 */
public class MobileERPApplication extends Application {

    private static Context context = null;
    private static String restUrl = null;
    private static SQLiteOpenHelper databaseHelper = null;
    private static final Object lockObject = new Object();

    public void onCreate() {
        super.onCreate();
        MobileERPApplication.context = getApplicationContext();

        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .delayBeforeLoading(50)
                .cloneFrom(DisplayImageOptions.createSimple())
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .discCacheFileCount(100)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);

        if (!BuildConfig.DEBUG) {
            com.nostra13.universalimageloader.utils.L.disableLogging();
        }
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static Context getContext() {
        return MobileERPApplication.context;
    }
    
    public static String getRestUrl() {
        if (restUrl == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean useCustomUrl = preferences.getBoolean(Constants.SETTING_USE_CUSTOM_URL, false);
            
            synchronized (lockObject) {
                if (useCustomUrl) {            
                    restUrl = preferences.getString(Constants.SETTING_SERVER_ADDR, Constants.REST_URL);
                } else {
                    restUrl = Constants.REST_URL;
                }
            }
        }
        return restUrl;
    }
    
    public static SQLiteOpenHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = new MobileErpDbHelper(getContext());
        } 
        return databaseHelper;
    }
    
}
