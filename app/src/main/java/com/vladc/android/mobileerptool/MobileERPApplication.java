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

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.File;
import java.io.IOException;

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

    public static DefaultHttpClient getHttpClient() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        DefaultHttpClient defaultHttpClient = (DefaultHttpClient) factory.getHttpClient();
        defaultHttpClient.addRequestInterceptor(new AuthRequestInterceptor(preferences));
        final boolean isProxyEnabled = preferences.getBoolean(Constants.SETTING_HTTP_PROXY_ENABLED, false);
        if (isProxyEnabled) {
            final String proxyAddr = preferences.getString(Constants.SETTING_HTTP_PROXY_ADDR, "127.0.0.1");
            final int proxyPort = preferences.getInt(Constants.SETTING_HTTP_PROXY_PORT, 3128);
            defaultHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(proxyAddr, proxyPort));
        }

        return defaultHttpClient;
    }

    private static class AuthRequestInterceptor implements HttpRequestInterceptor {

        private SharedPreferences prefs;

        public AuthRequestInterceptor(SharedPreferences prefs) {
            super();
            this.prefs = prefs;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            /* replace Accept header */
            request.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE + "," + new MediaType("image", "*", 0.8));

//            boolean isUserAuthenticated = this.prefs.getBoolean(Constants.KEY_USER_LOGGED_IN_STATE, false);
//            if (isUserAuthenticated) {
//                final String userAuthToken = this.prefs.getString(Constants.KEY_CURRENT_LOGGED_IN_USER_TOKEN, "");
//                request.setHeader(Constants.HttpHeaders.HEADER_AUTH_TOKEN, userAuthToken);
//                final int userAuthId = this.prefs.getInt(Constants.KEY_CURRENT_LOGGED_IN_USER_ID, 0);
//                request.setHeader(Constants.HttpHeaders.HEADER_AUTH_USER_ID, userAuthId + "");
//                final String androidDeviceId = getDeviceId(); //TODO get device id somehow
//                request.setHeader(Constants.HttpHeaders.HEADER_AUTH_DEVICE_ID, androidDeviceId);
//            }
        }
    }
    
}
