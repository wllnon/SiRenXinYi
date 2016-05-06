package com.test.wllnon.sirenxinyi.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.utils.AnimationUtils;
import com.test.wllnon.sirenxinyi.event.ReceivedDataMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ECGActivity extends AppCompatActivity
        implements View.OnClickListener, OnChartValueSelectedListener, OnChartGestureListener {

    private static final String TAG = "ecg";
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fullScreenButton;
    private FloatingActionButton selectModelButton;
    private FloatingActionButton followModelButton;
    private FloatingActionButton stopButton;

    private Toolbar toolbar;

    private LineChart lineChart;
    private LineDataSet dataSet;
    private LineData lineData;
    private List<Highlight> highlights = new ArrayList<>();

    private boolean isSelectedModel = false;
    private boolean isFullScreen = false;
    private boolean isFollowModel = true;
    private boolean isStopped = false;
    private boolean isFABHidden = false;

    private boolean isDuringGesture = false;

    private int maxXRange = 100;

    private AnimationUtils animator = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViews();
        bindListeners();

        chartSetting();

        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_ecg);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViews();
        bindListeners();

        chartSetting();
        
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            toolbar.setVisibility(View.GONE);
            isFullScreen = true;
            fullScreenButton.setIcon(R.drawable.ic_fullscreen_exit_black_18dp);

            maxXRange = 300;
        } else {
            toolbar.setVisibility(View.VISIBLE);
            isFullScreen = false;
            fullScreenButton.setIcon(R.drawable.ic_fullscreen_black_18dp);
            maxXRange = 100;
        }
    }
    
    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop: ");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(final ReceivedDataMessageEvent event){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int xValue = lineData.getXValCount();
                float yValue = event.receivedData;
                lineData.addXValue("" + xValue);
                lineData.addEntry(new Entry(yValue, xValue), 0);

                lineChart.setVisibleXRange(0, maxXRange);

                if (isFollowModel) {
                    lineChart.moveViewToX(lineChart.getXValCount() - maxXRange - 1);
                }

                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }
        });
    }

    private void chartSetting() {
        if (animator == null) {
            animator = AnimationUtils.getInstance(getApplicationContext());
        }

        if (dataSet == null) {
            // set the name of the chart line
            dataSet = new LineDataSet(new ArrayList<Entry>(), "Dynamic ECG");
            // set axis dependency of y-axis at right side
            dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            // set some colors of this chart line
            dataSet.setColor(getResources().getColor(R.color.colorAccent));
            dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimary));
            dataSet.setCircleColorHole(getResources().getColor(R.color.colorLightPrimary));
        }

//        if (cmpDataSet == null) {
//            // set the name of the chart line
//            cmpDataSet = new LineDataSet(new ArrayList<Entry>(), "Compare ECG");
//            // set axis dependency of y-axis at right side
//            cmpDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
//            // set some colors of this chart line
//            cmpDataSet.setColor(getResources().getColor(R.color.colorTranslate));
//            cmpDataSet.setValueTextColor(getResources().getColor(R.color.colorTranslate));
//            cmpDataSet.setCircleColorHole(getResources().getColor(R.color.colorTranslate));
//        }

        if (lineData == null) {
            // this data-sets is the collection of lines, but we just need one line only
            List<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);
//            dataSets.add(cmpDataSet);

            // create line-date which will be set into the chart.
            lineData = new LineData(new ArrayList<String>(), dataSets);
        }

        // chart setting...
        lineChart.setData(lineData);
        lineChart.setNoDataText(getString(R.string.no_data_text));
        lineChart.setBackgroundColor(getResources().getColor(R.color.colorBackground));
        lineChart.setGridBackgroundColor(getResources().getColor(R.color.colorBackground));

        lineChart.setMarkerView(new MyCustomMarkerView(ECGActivity.this, R.layout.marker_view_pop_up));

        lineChart.setOnChartValueSelectedListener(this);
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(getResources().getColor(R.color.colorAccent));
        lineChart.setBorderWidth(1.0f);

        lineChart.setDragDecelerationFrictionCoef(1.0f);

        lineChart.setVisibleXRange(0, maxXRange);

        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setValueFormatter(new MyCustomYAxisValueFormatter());
        leftYAxis.setGridColor(getResources().getColor(R.color.colorGray));
        leftYAxis.setTextColor(getResources().getColor(R.color.colorPrimary));

        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setValueFormatter(new MyCustomYAxisValueFormatter());
        rightYAxis.setGridColor(getResources().getColor(R.color.colorGray));
        rightYAxis.setTextColor(getResources().getColor(R.color.colorPrimary));

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGridColor(getResources().getColor(R.color.colorGray));
        xAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void findViews() {
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu_ecg);
        fullScreenButton = (FloatingActionButton) findViewById(R.id.full_screen_ecg);
        selectModelButton = (FloatingActionButton) findViewById(R.id.select_model_ecg);
        followModelButton = (FloatingActionButton) findViewById(R.id.follow_ecg);
        stopButton = (FloatingActionButton) findViewById(R.id.stop_ecg);
        lineChart = (LineChart) findViewById(R.id.chart_ecg);
    }

    private void bindListeners() {
        fullScreenButton.setOnClickListener(this);
        selectModelButton.setOnClickListener(this);
        followModelButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        lineChart.setOnChartGestureListener(this);
    }

    private void selectModelClick() {
        // TODO: 2016/4/2 do something
        isSelectedModel = !isSelectedModel;
        if (isSelectedModel) {
            animator.animateFAB(selectModelButton, R.drawable.ic_done_all_black_18dp, "Multi model.");
        } else {
            // clear the high-lights out
            lineChart.highlightValue(-1, -1);
            animator.animateFAB(selectModelButton, R.drawable.ic_done_black_18dp, "One model.");
        }
    }

    private void fullScreenClick() {
        // TODO: 2016/4/2 do something
        isFullScreen = !isFullScreen;
        if (isFullScreen) {
            animator.animateFAB(fullScreenButton, R.drawable.ic_fullscreen_exit_black_18dp, "FullScreen model.");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            animator.animateFAB(fullScreenButton, R.drawable.ic_fullscreen_black_18dp, "Normal model.");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void stopClick() {
        if (isStopped) {
            new Thread() {
                @Override
                public void run() {
                    final float unit = 0.1f;
                    for (int i = 0; i < 1024; ++i) {
                        if (!isStopped)
                            EventBus.getDefault().post(new ReceivedDataMessageEvent((float) Math.cos(i * unit)));
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        isStopped = !isStopped;
        if (isStopped) {
            animator.animateFAB(stopButton, R.drawable.ic_play_circle_filled_black_18dp, "Play.");
        } else {
            animator.animateFAB(stopButton, R.drawable.ic_pause_circle_filled_black_18dp, "Stop.");
        }
    }

    private void followModelClick() {
        isFollowModel = !isFollowModel;
        if (isFollowModel) {
            animator.animateFAB(followModelButton, R.drawable.ic_label_black_18dp, "Following.");
        } else {
            animator.animateFAB(followModelButton, R.drawable.ic_label_outline_black_18dp, "Follow.");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_model_ecg:
                selectModelClick();
                break;
            case R.id.full_screen_ecg:
                fullScreenClick();
                break;
            case R.id.stop_ecg:
                stopClick();
                break;
            case R.id.follow_ecg:
                followModelClick();
                break;
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i(TAG, "onValueSelected: ");
        if (isSelectedModel && dataSetIndex == 0) {
            boolean success = true;
            for (int i = 0; i < highlights.size(); ++i) {
                if (highlights.get(i).equalTo(h)) {
                    success = false;
                    highlights.remove(i);
                    break;
                }
            }
            if (success) {
                highlights.add(h);
            }

            MyHighlight[] highlights1 = new MyHighlight[highlights.size()];
            for (int i = 0; i < highlights1.length; ++i) {
                highlights1[i] = new MyHighlight(highlights.get(i), i, i == 0 ? null : highlights1[i-1]);
            }
            lineChart.highlightValues(highlights1);
        }
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "onNothingSelected: ");
        if (isSelectedModel) {
            MyHighlight[] highlights1 = new MyHighlight[highlights.size()];
            for (int i = 0; i < highlights1.length; ++i) {
                highlights1[i] = new MyHighlight(highlights.get(i), i, i == 0 ? null : highlights1[i-1]);
            }
            lineChart.highlightValues(highlights1);
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i(TAG, "onChartGestureStart: ");
        isDuringGesture = true;
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i(TAG, "onChartGestureEnd: ");
        isDuringGesture = false;
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i(TAG, "onChartLongPressed: ");
        if (isDuringGesture) {
            isFABHidden = !isFABHidden;
            if (isFABHidden) {
                AnimationUtils.getInstance(getApplicationContext()).animateOut(fabMenu, 0);
            } else {
                AnimationUtils.getInstance(getApplicationContext()).animateIn(fabMenu, 0);
            }
        }
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i(TAG, "onChartDoubleTapped: ");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i(TAG, "onChartSingleTapped: ");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i(TAG, "onChartFling: ");
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i(TAG, "onChartScale: ");
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        // Log.i(TAG, "onChartTranslate: ");
    }

    public class MyCustomYAxisValueFormatter implements YAxisValueFormatter {
        private DecimalFormat decimalFormat;

        MyCustomYAxisValueFormatter() {
            decimalFormat = new DecimalFormat("0.0");
        }

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            return decimalFormat.format(value) + "v";
        }
    }

    public class MyCustomMarkerView extends MarkerView {
        private TextView contentView;
        private TextView numberView;
        private DecimalFormat decimalFormat;

        public MyCustomMarkerView(Context context, int layoutId) {
            super(context, layoutId);
            contentView = (TextView) findViewById(R.id.content);
            numberView = (TextView) findViewById(R.id.number);
            decimalFormat = new DecimalFormat("0.000");
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            int currX = e.getXIndex();
            float currY = e.getVal();
            if (highlight instanceof MyHighlight) {
                MyHighlight myHighlight = (MyHighlight) highlight;
                if (myHighlight.getPreHighlight() != null) {
                    numberView.setText("No. " + myHighlight.getNumber() + ", Pre. " + myHighlight.getPreHighlight().getNumber());
                    int preX = myHighlight.getPreHighlight().getXIndex();
                    float preY = dataSet.getYValForXIndex(preX);
                    int xDiff = currX - preX;
                    float yDiff = currY - preY;

                    contentView.setText("x: " + currX + "s, y: " + decimalFormat.format(currY) + "v\n" +
                            "△x: " + xDiff + "s, △y: " + decimalFormat.format(yDiff) + "v\n" +
                            "1/△x: " + decimalFormat.format(1.0f / xDiff) + "Hz\n" +
                            "△y/△x: " + decimalFormat.format(yDiff / xDiff));
                } else {
                    numberView.setText("No. " + myHighlight.getNumber());
                    contentView.setText("x: " + currX + "s, y: " + decimalFormat.format(currY) + "v");
                }
            } else {
                numberView.setText("");
                contentView.setText("x: " + currX + "s, y: " + decimalFormat.format(currY) + "v");
            }

        }

        @Override
        public int getXOffset(float xpos) {
            return -(getWidth()/2);
        }

        @Override
        public int getYOffset(float ypos) {
            return 0;
        }
    }

    public class MyHighlight extends Highlight {
        private int number;
        private MyHighlight preHighlight;

        public MyHighlight(int x, int dataSet) {
            super(x, dataSet);
        }

        public MyHighlight(int x, int dataSet, int stackIndex) {
            super(x, dataSet, stackIndex);
        }

        public MyHighlight(int x, int dataSet, int stackIndex, Range range) {
            super(x, dataSet, stackIndex, range);
        }

        public MyHighlight(Highlight highlight, int number, MyHighlight preHighlight) {
            super(highlight.getXIndex(), highlight.getDataSetIndex(), highlight.getStackIndex(), highlight.getRange());
            this.number = number;
            this.preHighlight = preHighlight;
        }

        public int getNumber() {
            return number;
        }

        public MyHighlight getPreHighlight() {
            return preHighlight;
        }
    }
}
