package com.test.wllnon.sirenxinyi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.application.Application;
import com.test.wllnon.sirenxinyi.constant.Constant;

import java.text.DecimalFormat;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_SELECT_UPLOAD_TIME_SELECT = 1;

    public static final int RESULT_CANCEL = 100;
    public static final int RESULT_SUCCESS = 101;

    private Toolbar toolbar;

    private RelativeLayout autoLogin;
    private Switch autoLoginSW;

    private RelativeLayout defaultModel;
    private Switch defaultModelSW;

    private RelativeLayout backgroundService;
    private Switch backgroundServiceSW;

    private RelativeLayout accountPassword;

    private RelativeLayout blackList;

    private RelativeLayout autoUpload;
    private Switch autoUploadSW;

    private RelativeLayout autoUploadTimeSelector;
    private TextView autoUploadTimeSelectorDescription;

    private RelativeLayout uploadNow;

    private RelativeLayout ecgCache;
    private TextView ecgCacheDescription;

    private RelativeLayout msgCache;
    private TextView msgCacheDescription;

    private RelativeLayout clearCache;

    private SharedPreferences sharedPreferences;

    private boolean isAutoLogin;
    private boolean isDefaultModel;
    private boolean isBackgroundService;
    private boolean isAutoUpload;

    private String autoUploadTime;
    private int ecgCacheDays;
    private int msgCacheDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViews();
        setListeners();
        configViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storeSettings();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        autoLogin   = (RelativeLayout)  findViewById(R.id.auto_login_setting);
        autoLoginSW = (Switch)          findViewById(R.id.auto_login_switch_setting);

        defaultModel    = (RelativeLayout)  findViewById(R.id.default_model_setting);
        defaultModelSW  = (Switch)          findViewById(R.id.default_model_switch_setting);

        backgroundService   = (RelativeLayout)  findViewById(R.id.background_service_setting);
        backgroundServiceSW = (Switch)          findViewById(R.id.background_service_switch_setting);

        accountPassword = (RelativeLayout) findViewById(R.id.account_password_setting);

        blackList = (RelativeLayout) findViewById(R.id.black_list_setting);

        autoUpload      = (RelativeLayout)  findViewById(R.id.auto_upload_model_setting);
        autoUploadSW    = (Switch)          findViewById(R.id.auto_upload_switch_model_setting);

        autoUploadTimeSelector              = (RelativeLayout)  findViewById(R.id.auto_upload_time_selector_setting);
        autoUploadTimeSelectorDescription   = (TextView)        findViewById(R.id.auto_upload_time_selector_description);

        uploadNow   = (RelativeLayout) findViewById(R.id.upload_now_setting);

        ecgCache            = (RelativeLayout)  findViewById(R.id.ecg_cache_setting);
        ecgCacheDescription = (TextView)        findViewById(R.id.ecg_cache_description);

        msgCache            = (RelativeLayout)  findViewById(R.id.message_cache_setting);
        msgCacheDescription = (TextView)        findViewById(R.id.msg_cache_description);

        clearCache  = (RelativeLayout) findViewById(R.id.clear_cache_setting);
    }

    private void setListeners() {
        autoLogin.setOnClickListener(this);
        autoLoginSW.setOnClickListener(this);

        defaultModel.setOnClickListener(this);
        defaultModelSW.setOnClickListener(this);

        backgroundService.setOnClickListener(this);
        backgroundServiceSW.setOnClickListener(this);

        accountPassword.setOnClickListener(this);

        blackList.setOnClickListener(this);

        autoUpload.setOnClickListener(this);
        autoUploadSW.setOnClickListener(this);

        autoUploadTimeSelector.setOnClickListener(this);

        uploadNow.setOnClickListener(this);

        ecgCache.setOnClickListener(this);

        msgCache.setOnClickListener(this);

        clearCache.setOnClickListener(this);
    }

    private void configViews() {
        sharedPreferences = Application.getInstance().getSharedPreferences();

        isAutoLogin = sharedPreferences.getBoolean(Constant.AUTO_LOGIN, false);
        autoLoginSW.setChecked(isAutoLogin);

        isDefaultModel = sharedPreferences.getBoolean(Constant.DEFAULT_MODEL, false);
        defaultModelSW.setChecked(isDefaultModel);
        backgroundServiceSW.setEnabled(isDefaultModel);

        isBackgroundService = sharedPreferences.getBoolean(Constant.BACKGROUND_SERVICE, false);
        backgroundServiceSW.setChecked(isBackgroundService);

        isAutoUpload = sharedPreferences.getBoolean(Constant.AUTO_UPLOAD, false);
        autoUploadSW.setChecked(isAutoUpload);

        autoUploadTime = sharedPreferences.getString(Constant.AUTO_UPLOAD_TIME, "");
        autoUploadTimeSelectorDescription.setText(TextUtils.isEmpty(autoUploadTime) ? "--:--" : autoUploadTime);

        ecgCacheDays = sharedPreferences.getInt(Constant.ECG_CACHE_DAYS, Constant.DEFAULT_ECG_CACHE_DAYS);
        ecgCacheDescription.setText(ecgCacheDays + "天");
        msgCacheDays = sharedPreferences.getInt(Constant.MSG_CACHE_DAYS, Constant.DEFAULT_MSG_CACHE_DAYS);
        msgCacheDescription.setText(ecgCacheDays + "天");
    }

    private void storeSettings() {
        sharedPreferences = Application.getInstance().getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(Constant.AUTO_LOGIN, isAutoLogin);
        editor.putBoolean(Constant.DEFAULT_MODEL, isDefaultModel);
        editor.putBoolean(Constant.BACKGROUND_SERVICE, isBackgroundService);
        editor.putBoolean(Constant.AUTO_UPLOAD, isAutoUpload);

        editor.putString(Constant.AUTO_UPLOAD_TIME, autoUploadTime);
        editor.putInt(Constant.ECG_CACHE_DAYS, ecgCacheDays);
        editor.putInt(Constant.MSG_CACHE_DAYS, msgCacheDays);

        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_UPLOAD_TIME_SELECT:
                if (resultCode == RESULT_SUCCESS) {
                    if (data != null) {
                        int hour = data.getIntExtra(TimeSelectorActivity.HOUR, -1);
                        int minute = data.getIntExtra(TimeSelectorActivity.MINUTE, -1);
                        if (hour == -1 || minute == -1) {
                            autoUploadTime = "--:--";
                        } else {
                            DecimalFormat decimalFormat = new DecimalFormat("00");
                            autoUploadTime = decimalFormat.format(hour) + ":" + decimalFormat.format(minute);
                        }
                        autoUploadTimeSelectorDescription.setText(autoUploadTime);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auto_login_setting:
                autoLoginSW.toggle();
            case R.id.auto_login_switch_setting:
                isAutoLogin = !isAutoLogin;
                break;

            case R.id.default_model_setting:
                defaultModelSW.toggle();
            case R.id.default_model_switch_setting:
                isDefaultModel = !isDefaultModel;
                backgroundServiceSW.setEnabled(isDefaultModel);
                break;

            case R.id.background_service_setting:
                if (isDefaultModel) {
                    backgroundServiceSW.toggle();
                }
            case R.id.background_service_switch_setting:
                if (isDefaultModel) {
                    isBackgroundService = !isBackgroundService;
                }
                break;

            case R.id.account_password_setting:
                break;
            case R.id.black_list_setting:
                break;

            case R.id.auto_upload_model_setting:
                autoUploadSW.toggle();
            case R.id.auto_upload_switch_model_setting:
                isAutoUpload = !isAutoUpload;
                break;

            case R.id.auto_upload_time_selector_setting:
                startActivityForResult(new Intent(this, TimeSelectorActivity.class), REQUEST_SELECT_UPLOAD_TIME_SELECT);
                break;
            case R.id.upload_now_setting:
                break;
            case R.id.ecg_cache_setting:
                break;
            case R.id.message_cache_setting:
                break;
            case R.id.clear_cache_setting:
                break;
        }
    }
}
