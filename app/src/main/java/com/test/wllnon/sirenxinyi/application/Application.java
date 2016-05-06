package com.test.wllnon.sirenxinyi.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.activity.StartActivity;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.utils.network.NetworkUtils;
import com.test.wllnon.sirenxinyi.utils.SystemUtil;

import java.io.File;
import java.io.IOException;


/**
 * Created by Administrator on 2016/4/19.
 */
public class Application extends android.app.Application {
    public static final String TAG = "Application";

    public static String sharedPreferenceName = "SiRenXinYiSP";

    private static Application instance;
    private NetworkUtils networkUtils;

    private SharedPreferences sharedPreferences;

    private boolean isLogined = false;
    private User user = null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        networkUtils = NetworkUtils.newInstance();
        sharedPreferences = getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);

        NIMClient.init(this, getLoginInfo(), getOptions());
        if (inMainProcess()) {
            // 注册语言变化监听
            registerLocaleReceiver(true);
        }
    }

    private LoginInfo getLoginInfo() {
        return null;
//        return new LoginInfo("wllnon", "wllnon");
    }

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();

        // 点击通知需要跳转到的界面
        config.notificationEntrance = StartActivity.class;
        config.notificationSmallIconId = R.drawable.ic_launcher;

        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        File destDir = new File(ContextCompat.getExternalFilesDirs(this, null)[0].toString()  + "/nim/log");
        if (!destDir.exists()) {
            if (destDir.mkdirs()) {
                Log.i(TAG, "getOptions: true");
            } else {
                Log.i(TAG, "getOptions: false");
            }
        }
        File file = new File(destDir, "nim_sdk.log");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 配置保存图片，文件，log等数据的目录
        options.sdkStorageRootPath = ContextCompat.getExternalFilesDirs(this, null)[0].toString() + "/nim";

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 用户信息提供者
        options.userInfoProvider = userInfoProvider;

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;

        return options;
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    public static synchronized Application getInstance() {
        return instance;
    }

    public synchronized NetworkUtils getNetworkUtils() {
        return networkUtils;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setLogined(boolean isLogined) {
        this.isLogined = isLogined;
    }

    public boolean isLogined() {
        return isLogined;
    }

    private void registerLocaleReceiver(boolean register) {
        if (register) {
            updateLocale();
            IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(localeReceiver, filter);
        } else {
            unregisterReceiver(localeReceiver);
        }
    }

    private BroadcastReceiver localeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                updateLocale();
            }
        }
    };

    private void updateLocale() {
        NimStrings strings = new NimStrings();
        strings.status_bar_multi_messages_incoming = getString(R.string.nim_status_bar_multi_messages_incoming);
        strings.status_bar_image_message = getString(R.string.nim_status_bar_image_message);
        strings.status_bar_audio_message = getString(R.string.nim_status_bar_audio_message);
        strings.status_bar_custom_message = getString(R.string.nim_status_bar_custom_message);
        strings.status_bar_file_message = getString(R.string.nim_status_bar_file_message);
        strings.status_bar_location_message = getString(R.string.nim_status_bar_location_message);
        strings.status_bar_notification_message = getString(R.string.nim_status_bar_notification_message);
        strings.status_bar_ticker_text = getString(R.string.nim_status_bar_ticker_text);
        strings.status_bar_unsupported_message = getString(R.string.nim_status_bar_unsupported_message);
        strings.status_bar_video_message = getString(R.string.nim_status_bar_video_message);
        strings.status_bar_hidden_message_content = getString(R.string.nim_status_bar_hidden_msg_content);
        NIMClient.updateStrings(strings);
    }

    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }
    };

    private UserInfoProvider userInfoProvider = new UserInfoProvider() {
        @Override
        public UserInfo getUserInfo(String account) {
            return new UserInfo() {
                @Override
                public String getAccount() {
                    return "wllnon";
                }

                @Override
                public String getName() {
                    return "wllnon";
                }

                @Override
                public String getAvatar() {
                    return "http://imgsrc.baidu.com/forum/w%3D580/sign=529f09a44bed2e73fce98624b700a16d/45921efa513d26973d0bee7d54fbb2fb4216d8b1.jpg";
                }
            };
        }

        @Override
        public int getDefaultIconResId() {
            return R.drawable.ic_account_circle_grey_600_48dp;
        }

        @Override
        public Bitmap getTeamIcon(String tid) {
            return null;
        }

        @Override
        public Bitmap getAvatarForMessageNotifier(String account) {
            return null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            return "TextMessage Notifier";
        }
    };

}
