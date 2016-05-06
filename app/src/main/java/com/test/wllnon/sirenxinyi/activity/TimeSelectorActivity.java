package com.test.wllnon.sirenxinyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.test.wllnon.sirenxinyi.R;

public class TimeSelectorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";

    private TimePicker timePicker;
    private Button cancel;
    private Button confirm;

    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_selector);

        setResult(SettingActivity.RESULT_CANCEL);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        assert timePicker != null;
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfHour) {
                hour = hourOfDay;
                minute = minuteOfHour;
            }
        });

        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();

        cancel = (Button) findViewById(R.id.cancel_time_piker);
        confirm = (Button) findViewById(R.id.confirm_time_piker);
        assert cancel != null;
        assert confirm != null;
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_time_piker:
                finish();
                break;
            case R.id.confirm_time_piker:
                Intent intent = new Intent();
                intent.putExtra(HOUR, hour);
                intent.putExtra(MINUTE, minute);
                setResult(SettingActivity.RESULT_SUCCESS, intent);
                finish();
                break;
        }
    }
}
