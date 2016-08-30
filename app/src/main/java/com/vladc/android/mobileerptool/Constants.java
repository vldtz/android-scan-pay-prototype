package com.vladc.android.mobileerptool;

public final class Constants {

    /**
     * Address of catalog web application
     */
    public final static String REST_URL = "http://demo4260365.mockable.io/";
    
    public final static String SETTING_LANGUAGE = "application_language";

    public final static String SETTING_USE_CUSTOM_URL = "pref_use_custom_url";
    public final static String SETTING_SERVER_ADDR = "pref_server_addr";
    
    public final static String SETTING_HTTP_PROXY_ENABLED = "pref_proxy_enabled";
    public final static String SETTING_HTTP_PROXY_ADDR = "pref_http_proxy_addr";
    public final static String SETTING_HTTP_PROXY_PORT = "pref_http_proxy_port";
    
    public static final String KEY_CURRENT_LOGGED_IN_USER_ID = "currentLoggedInUserId";
    public static final String KEY_CURRENT_LOGGED_IN_USER_TOKEN = "currentLoggedInUserToken";
    public static final String KEY_CURRENT_LOGGED_IN_USER_TYPE = "currentLoggedInUserType";
    public static final String KEY_DEVICE_ID = "deviceId";


    public static final String KEY_USER_LOGGED_IN_STATE = "userLoggedInState";
    
    /** 
     * Holds name of headers added to each authenticated request  
     */
    public final class HttpHeaders {
        
        public final static String HEADER_AUTH_USER_ID = "User";
        public final static String HEADER_AUTH_DEVICE_ID = "Device";
        public final static String HEADER_AUTH_TOKEN = "Token";
    }
    
    public final static String ACTION_SYNC_BUTTON_REFRESH = "ro.edea.android.catalog.REFRESH_SYNC";
    
    public final static String CATEGORY_UI_ACTION = "ui_action";
    public final static String ACTION_BUTTON_CLICK = "button_click";
    public final static String ACTION_SYNC = "data_sync";
    
    public final static String CATEGORY_POST_TASK = "post_task_action";
    public final static String IMAGE_DELETE = "image_not_found";
    
    public final static String COMMUNITY_ID = "communityId";
    public final static String TEACHER_ID = "teacherId";
    public final static String STUDENT = "student";
}
