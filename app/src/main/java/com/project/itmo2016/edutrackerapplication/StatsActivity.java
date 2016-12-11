package com.project.itmo2016.edutrackerapplication;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.project.itmo2016.edutrackerapplication.models.statistics.Stats;
import com.project.itmo2016.edutrackerapplication.models.statistics.StatsDay;
import com.project.itmo2016.edutrackerapplication.utils.FileIOUtils;
import com.project.itmo2016.edutrackerapplication.utils.StatsUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Aleksandr Tukallo on 07.12.16.
 */

public class StatsActivity extends AppCompatActivity {

    Stats stats;

    public static final String TAG = "StatsActivity";

    /**
     * shiftUpwards is needed to distinguish days with zero attendance and days without any periods.
     * Days with zero attendance are shifted a bit upwards. As a result a red bar for them can be seen.
     */
    private static final float shiftUpwards = 0.2f;

    BarChart barChart;

    //TODO delete function when debug finished
    void tmpFunctionForDebugOnlyWhichEnfillsStatsToTestChart() {
        int[] numberOfPeriodsEachDay = {5, 6, 2, 3, 1, 2};

        for (int i = 0; i < numberOfPeriodsEachDay.length; i++) {
            ArrayList<Boolean> arr = new ArrayList<>();
            for (int j = 0; j < numberOfPeriodsEachDay[i]; j++) {
                arr.add((ThreadLocalRandom.current().nextInt(0, 2) == 0));
            }
            Date date = new Date(2016, 12, i + 12); //starts with 5 december, which is mon
            stats.attendanceHistory.add(new StatsDay(arr, date));
        }
    }

    //TODO add navbar to this activity

    /**
     * This method initializes stats.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //initializing stats
        stats = FileIOUtils.loadSerializableFromFile(getIntent().getExtras().getString(ScheduleActivity.EXTRA_PATH_TO_STATS), this);

        tmpFunctionForDebugOnlyWhichEnfillsStatsToTestChart(); //TODO tmp only
        if (stats == null) {
            Log.d(TAG, "unable to load stats from file!!! exiting activity");
            finish();
        }

        //displaying barChart
        setupBarChartWeek();
    }

    /**
     * Method setups barChart to display attendance within current week.
     * All data is generated here.
     */
    private void setupBarChartWeek() {
        Log.d(TAG, "creating barChart to display current week attendance");
        barChart = (BarChart) findViewById(R.id.bar_chart);

        List<BarEntry> entries = new ArrayList<BarEntry>(); //we will have 6 bars: for each day
        Calendar curDate = Calendar.getInstance();
        int[] colors = new int[6];

        int numberOfDaysPassedInCurWeek = curDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 1; //inclusive
        //todo tmp only to debug on sunday
        numberOfDaysPassedInCurWeek++;

        for (int i = 0; i < numberOfDaysPassedInCurWeek; i++) {
            float xValue = (float) i;
            float yValue = getYValue(stats.attendanceHistory
                    .get(stats.attendanceHistory.size() - numberOfDaysPassedInCurWeek + i).lessons); //we need to display current week only

            colors[i] = ContextCompat.getColor(this, StatsUtils.getColorForYValue(yValue, shiftUpwards));
            entries.add(new BarEntry(xValue, yValue));

            Log.d(TAG, Integer.toString(i) + "th entry: " + Float.toString(xValue) + " " + Float.toString(yValue));
        }

        for (int i = numberOfDaysPassedInCurWeek; i < 6; i++) { //fill chart up to 6 days with zeroes
            float xValue = (float) i;
            float yValue = 0.0f;
            //entries when no periods are not shifted -- no bars will be shown
            entries.add(new BarEntry(xValue, yValue));

            colors[i] = ContextCompat.getColor(this, R.color.grey); //color here just for fun. No bars are shown

            Log.d(TAG, Integer.toString(i) + "th entry: " + Float.toString(xValue) + " " + Float.toString(yValue));
        }

        BarDataSet dataSet = new BarDataSet(entries, "attendanceBars");
        dataSet.setColors(colors);

        //TODO working with x and y axis legend needed
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        //chart background is not white
        barChart.setDrawGridBackground(true);
        barChart.setGridBackgroundColor(ContextCompat.getColor(this, R.color.grey));
//        barChart.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));

        //disabling chart description
        Description descr = new Description();
        descr.setEnabled(false);
        barChart.setDescription(descr);

        //creates a bit of empty space on the sides of the chart
        barChart.setFitBars(true);

        //disabling background grid
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);

        //working with legend. It is set of colors below a chart
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

//        nb this method may be useful for VK
//        barChart.saveToGallery();

        //updating chart
        barChart.invalidate();
    }

    /**
     * gets an array with booleans (i.e. day) and returns y-value for such array to display in barChart
     */
    private float getYValue(ArrayList<Boolean> arr) {
        int counter = 0;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i))
                counter++;
        }
        return (float) counter / (float) arr.size() + shiftUpwards;
    }
}
