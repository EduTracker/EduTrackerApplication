package com.project.itmo2016.edutrackerapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.project.itmo2016.edutrackerapplication.models.Statistics.Stats;
import com.project.itmo2016.edutrackerapplication.models.Statistics.StatsDay;
import com.project.itmo2016.edutrackerapplication.utils.FileIOUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Aleksandr Tukallo on 07.12.16.
 */

public class StatsActivity extends AppCompatActivity {
    Stats stats;

    public static final String TAG = "StatsActivity";

    BarChart barChart;

    void tmpFunctionForDebugOnlyWhichEnfillsStatsToTestChart() {
        int[] numbreOfPeriodsEachDay = {5, 6, 2, 3, 1, 2}; //TODO add 0 periods to test

        for (int i = 0; i < numbreOfPeriodsEachDay.length; i++) {
            ArrayList<Boolean> arr = new ArrayList<>();
            for (int j = 0; j < numbreOfPeriodsEachDay[i]; j++) {
                arr.add((ThreadLocalRandom.current().nextInt(0, 2) == 0));
            }
            Date date = new Date(2016, 12, i + 5); //starts with 5 december, which is mon
            stats.attendanceHistory.add(new StatsDay(arr, date));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        stats = FileIOUtils.loadSerializableFromFile(getIntent().getExtras().getString(ScheduleActivity.EXTRA_PATH_TO_STATS), this);
        tmpFunctionForDebugOnlyWhichEnfillsStatsToTestChart(); //TODO tmp only
        if (stats == null) {
            Log.d(TAG, "unable to load stats from file!!! exiting activity");
            finish();
        }

        setupBarChart();
    }

    private void setupBarChart() {
        barChart = (BarChart) findViewById(R.id.bar_chart);
//        barChart.setOnChartValueSelectedListener(barChartValuesListener);

        List<BarEntry> entries = new ArrayList<BarEntry>();

        //TODO it's tmp, in real code we will use current week
        for (int i = 0; i < 6; i++) {
            entries.add(new BarEntry((float) i, getYValue(stats.attendanceHistory.get(i).lessons)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "bars");

        //TODO styling needed
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }

    /**
     * gets an array with booleans and returns y-value for such array (i.e. day) for barChart
     */
    private float getYValue(ArrayList<Boolean> arr) {
        int counter = 0;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i))
                counter++;
        }
        return (float) counter / (float) arr.size();
    }

//    private OnChartValueSelectedListener barChartValuesListener = new OnChartValueSelectedListener() {
//        @Override
//        public void onValueSelected(Entry e, Highlight h) {
//
//        }
//
//        @Override
//        public void onNothingSelected() {
//
//        }
//    }

}
