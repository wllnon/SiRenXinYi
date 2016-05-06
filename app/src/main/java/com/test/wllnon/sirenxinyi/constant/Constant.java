package com.test.wllnon.sirenxinyi.constant;

/**
 * Created by Administrator on 2016/3/30.
 */
public class Constant {

    public enum DeviceState {
        DEVICE_ON, DEVICE_OFF, DEVICE_WAIT
    }

    // Card types
    public final static int FOOTER      = 100;
    public final static int HEADER      = 101;
    public final static int ANSWER      = 1;
    public final static int FRIEND      = 2;
    public final static int MESSAGE     = 3;
    public final static int CHAT        = 4;
    public final static int CHAT_LEFT   = 5;
    public final static int COMMENT     = 6;
    public final static int QUESTION    = 7;

    public final static int CLASSIFY    = 8;
    public final static int DEVICE_ECG  = 9;


    // Setting constants
    public final static String AUTO_EMAIL = "auto_email";
    public final static String AUTO_PASSWORD = "auto_password";

    public final static String USERS_HISTORY = "users_history";

    public final static String AUTO_LOGIN = "auto_login";
    public final static String AUTO_UPLOAD = "auto_upload";
    public final static String AUTO_UPLOAD_TIME = "auto_update_time";

    public final static int DEFAULT_ECG_CACHE_DAYS = 5;
    public final static int DEFAULT_MSG_CACHE_DAYS = 5;
    public final static String ECG_CACHE_DAYS = "ecg_cache_days";
    public final static String MSG_CACHE_DAYS = "msg_cache_days";

    public final static String DEFAULT_MODEL = "default_model";
    public final static String BACKGROUND_SERVICE = "background_service";

}
