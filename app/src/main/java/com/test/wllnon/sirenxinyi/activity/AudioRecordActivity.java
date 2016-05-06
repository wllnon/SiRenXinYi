package com.test.wllnon.sirenxinyi.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.test.wllnon.sirenxinyi.R;

import java.io.File;

public class AudioRecordActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "AudioRecordActivity";

    public static final String FILE_AUDIO = "FILE_AUDIO";
    public static final String LENGTH_AUDIO = "LENGTH_AUDIO";

    private Chronometer chronometer;
    private Button cancelBtn;
    private Button controlBtn;

    private AudioRecorder audioRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);

        setResult(ChatActivity.RESULT_CANCEL);

        initFields();
        findViews();
        setListeners();
        configViews();
    }

    private void initFields() {
        audioRecorder = new AudioRecorder(this, RecordType.AAC, 60000, audioRecordCallback);
    }

    private void findViews() {
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        cancelBtn = (Button) findViewById(R.id.cancel_audio);
        controlBtn = (Button) findViewById(R.id.control_audio);
    }

    private void setListeners() {
        cancelBtn.setOnClickListener(this);
        controlBtn.setOnClickListener(this);
    }

    private void configViews() {
        controlModel(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_audio:
                audioRecorder.completeRecord(true);
                finish();
                break;
            case R.id.control_audio:
                if (view.getTag() == null) {
                    controlModel(false);
                    audioRecorder.startRecord();
                } else {
                    audioRecorder.completeRecord(false);
                }
                break;
        }
    }

    private void controlModel(boolean start) {
        if (start) {
            controlBtn.setTag(null);
            controlBtn.setText("开始");
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
        } else {
            controlBtn.setTag(true);
            controlBtn.setText("完成");
            chronometer.start();
        }
    }

    IAudioRecordCallback audioRecordCallback = new IAudioRecordCallback() {
        @Override
        public void onRecordReady() {
            Log.i(TAG, "onRecordReady: ");
            // no-op
        }

        @Override
        public void onRecordStart(File file, RecordType recordType) {
            Log.i(TAG, "onRecordStart: ");
            // no-op
        }

        @Override
        public void onRecordSuccess(File file, long l, RecordType recordType) {
            Log.i(TAG, "onRecordSuccess: ");
            Intent data = new Intent();
            data.putExtra(FILE_AUDIO, file);
            data.putExtra(LENGTH_AUDIO, l + 1000);
            setResult(ChatActivity.RESULT_SUCCESS, data);
            finish();
        }

        @Override
        public void onRecordFail() {
            Log.i(TAG, "onRecordFail: ");
            controlModel(true);
            Toast.makeText(AudioRecordActivity.this, "录音失败。。请稍后重试", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRecordCancel() {
            Log.i(TAG, "onRecordCancel: ");
            finish();
        }

        @Override
        public void onRecordReachedMaxTime(int i) {
            controlModel(true);
            Toast.makeText(AudioRecordActivity.this, "录音超时。。请稍后重试", Toast.LENGTH_SHORT).show();
        }
    };

}
