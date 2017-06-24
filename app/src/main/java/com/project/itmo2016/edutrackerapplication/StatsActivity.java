package com.project.itmo2016.edutrackerapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.project.itmo2016.edutrackerapplication.models.statistics.Stats;
import com.project.itmo2016.edutrackerapplication.models.statistics.StatsDay;
import com.project.itmo2016.edutrackerapplication.utils.FileIOUtils;
import com.project.itmo2016.edutrackerapplication.utils.StatsUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Aleksandr Tukallo on 07.12.16.
 */

public class StatsActivity extends Drawer {

    private Stats stats;

    public static final String TAG = "StatsActivity";

    /**
     * shiftUpwards is needed to distinguish days with zero attendance and days without any periods.
     * Days with zero attendance are shifted a bit upwards. As a result a red bar for them can be seen.
     */
    private static final float shiftUpwards = 0.2f;

    private BarChart barChart;
    private TextView errorNoData;
    private TextView headerText;

    private boolean isMonthDisplayed; //Current chart mode: 0 is week, 1 is month
    private GregorianCalendar timePeriodInChart;

    /**
     * Method needed for debug only.
     * Fills stats with random values from october to 3rd week of december
     */
    private void fillStatsRandomlyForDebug() {
        int[] numberOfPeriodsEachDay = {4, 5, 5, 4, 4, 4};

        GregorianCalendar dateToFillFrom = new GregorianCalendar(2016, Calendar.AUGUST, 1);
        GregorianCalendar rightNow = new GregorianCalendar();
        rightNow.add(isMonthDisplayed ? Calendar.MONTH : Calendar.WEEK_OF_YEAR, -1);

        //sundays are never in statistics
        if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            rightNow.add(Calendar.DAY_OF_WEEK, -1);
        }

        while (dateToFillFrom.get(Calendar.YEAR) != rightNow.get(Calendar.YEAR)
                || dateToFillFrom.get(Calendar.MONTH) != rightNow.get(Calendar.MONTH)
                || dateToFillFrom.get(Calendar.DATE) != rightNow.get(Calendar.DATE)) {

            if (dateToFillFrom.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ArrayList<Boolean> arr = new ArrayList<>();
                for (int j = 0; j < numberOfPeriodsEachDay[dateToFillFrom.get(Calendar.DAY_OF_WEEK) - 2]; j++) {
                    arr.add((ThreadLocalRandom.current().nextInt(0, 2) == 0));
                }
                stats.attendanceHistory.add(new StatsDay(arr, StatsUtils.copyCalendarConstructor(dateToFillFrom)));
            }

            dateToFillFrom.add(Calendar.DATE, 1);
        }
    }

    /**
     * This method initializes stats.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_stats, null, false);
        drawer.addView(contentView, 0);

        //initializing stats
        stats = FileIOUtils.loadSerializableFromFile(getIntent().getExtras().getString(ScheduleActivity.EXTRA_PATH_TO_STATS), this);
        fillStatsRandomlyForDebug(); //TODO tmp only, to show cool charts

        if (stats == null) {
            Log.d(TAG, "unable to load stats from file!!! exiting activity");
            finish();
        }

        barChart = (BarChart) findViewById(R.id.bar_chart);
        errorNoData = (TextView) findViewById(R.id.error_no_data);
        headerText = (TextView) findViewById(R.id.header_stats_text);
        Button next = (Button) findViewById(R.id.next);
        Button prev = (Button) findViewById(R.id.previous);
        Button switchButton = (Button) findViewById(R.id.switch_button);

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMonthDisplayed) {
                    isMonthDisplayed = false;
                    setupBarChart(StatsUtils.ensureNotSunday(timePeriodInChart), false);
                } else {
                    isMonthDisplayed = true;
                    setupBarChart(StatsUtils.ensureNotSunday(timePeriodInChart), true);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMonthDisplayed) {
                    timePeriodInChart.add(Calendar.MONTH, 1);
                    setupBarChart(StatsUtils.ensureNotSunday(timePeriodInChart), true);
                } else {
                    timePeriodInChart.add(Calendar.DATE, 7);
                    setupBarChart(StatsUtils.ensureNotSunday(timePeriodInChart), false);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMonthDisplayed) {
                    timePeriodInChart.add(Calendar.MONTH, -1);
                    setupBarChart(StatsUtils.ensureNotSunday(timePeriodInChart), true);
                } else {
                    timePeriodInChart.add(Calendar.DATE, -7);
                    setupBarChart(StatsUtils.ensureNotSunday(timePeriodInChart), false);
                }
            }
        });

        //displaying barChart
        setupBarChart(StatsUtils.ensureNotSunday(new GregorianCalendar()), false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.schedule) {
            final Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Pre: period is not SUNDAY, use ensureNotSunday method in StatsUtils
     */
    private void setupBarChart(final GregorianCalendar period, boolean isMonth) {
        if (isMonth) {
            setupBarChartMonth(period);
        } else setupBarChartWeek(period);

        //disabling chart description
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        barChart.getBarData().setDrawValues(false);

        //creates a bit of empty space on the sides of the chart
        barChart.setFitBars(true);

        //disabling background grid
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
//        barChart.getAxisRight().setDrawGridLines(false);

        //working with legend. It is set of colors below a chart
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        XAxis xaxis = barChart.getXAxis();
        if (isMonth) {
            xaxis.setLabelCount(15);
        } else {
            xaxis.setLabelCount(6);
        }
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yLAxis = barChart.getAxisLeft();
        yLAxis.setAxisMaximum(1.4f);
        yLAxis.setAxisMinimum(0.0f);
        yLAxis.setLabelCount(4);
        yLAxis.setDrawZeroLine(true);
        yLAxis.setGranularityEnabled(true);
        yLAxis.setGranularity(0.25f);

        YAxis yRAxis = barChart.getAxisRight();
        yRAxis.setAxisMaximum(1.4f);
        yRAxis.setAxisMinimum(0.0f);
        yRAxis.setLabelCount(4);
        yRAxis.setDrawZeroLine(true);
        yRAxis.setGranularityEnabled(true);
        yRAxis.setGranularity(0.25f);

        yLAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Float.toString(value / 1.2f);
            }
        });
        yRAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Float.toString(value / 1.2f);
            }
        });

        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);

        //updating chart
        barChart.invalidate();
    }

    //pre: not Sunday
    private void setupBarChartWeek(final GregorianCalendar week) {
        Log.d(TAG, "creating barChart to display current week attendance");

        isMonthDisplayed = false;
        timePeriodInChart = week;

        StatsUtils.GeneratedData<BarDataSet> generated = dataGenerator(week, 6);

        BarData barData = new BarData(generated.chartDataSet);
        barChart.setData(barData);

        int dateFrom, dateTo;
        StatsUtils.moveToMonday(week);
        dateFrom = week.get(Calendar.DATE);

        StringBuilder text = new StringBuilder("Week: ");
        text.append(Integer.toString(dateFrom)).append(" ").append(week.getDisplayName(Calendar.MONTH, Calendar.SHORT, new Locale("US")));

        week.add(Calendar.DATE, 5); //go to saturday
        dateTo = week.get(Calendar.DATE);

        text.append(" - ").append(dateTo).append(" ").append(week.getDisplayName(Calendar.MONTH, Calendar.SHORT, new Locale("US"))).append(" ").append(week.get(Calendar.YEAR));
        headerText.setText(text.toString());

        if (generated.noData) {
            errorNoData.setText(R.string.error_no_data_for_week);
            errorNoData.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.INVISIBLE);
        } else {
            errorNoData.setVisibility(View.INVISIBLE);
            barChart.setVisibility(View.VISIBLE);
        }
    }

    //pre: not Sunday
    private void setupBarChartMonth(final GregorianCalendar month) {
        Log.d(TAG, "creating barChart to display current month attendance");

        isMonthDisplayed = true;
        timePeriodInChart = month;

        StatsUtils.GeneratedData<BarDataSet> generated = dataGenerator(month, month.getActualMaximum(Calendar.DATE));

        BarData barData = new BarData(generated.chartDataSet);
        barChart.setData(barData);

        headerText.setText("Month: 1 " + month.getDisplayName(Calendar.MONTH, Calendar.SHORT, new Locale("US")) + " - "
                + Integer.toString(month.getActualMaximum(Calendar.DATE)) + " "
                + month.getDisplayName(Calendar.MONTH, Calendar.SHORT, new Locale("US"))
                + " " + month.get(Calendar.YEAR));

        if (generated.noData) {
            errorNoData.setText(R.string.error_no_data_for_month);
            errorNoData.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.INVISIBLE);
        } else {
            errorNoData.setVisibility(View.INVISIBLE);
            barChart.setVisibility(View.VISIBLE);
        }
    }

    private int getIndex(int i, boolean isMonth) {
        if (!isMonth) {
            return stats.attendanceHistory.get(i).date.get(Calendar.DAY_OF_WEEK) - 2;
        } else {
            return stats.attendanceHistory.get(i).date.get(Calendar.DATE) - 1;
        }
    }

    private StatsUtils.GeneratedData<BarDataSet> dataGenerator(final GregorianCalendar period, final int length) {
        boolean isMonth = length != 6;

        List<BarEntry> entries = new ArrayList<>(); //we will have bars for each day
        for (int i = 0; i < length; i++) {
            entries.add(new BarEntry(0, 0));
        }

        int[] colors = new int[length];
        boolean[] filled = new boolean[length];
        boolean noData = true;

        //data here is generated using linear search. It is not the most effective method, but size of stats will never be more, than 1e4, so it's ok

        for (int i = 0; i < stats.attendanceHistory.size(); i++) {
            if (stats.attendanceHistory.get(i).date.get(Calendar.YEAR) == period.get(Calendar.YEAR)
                    && stats.attendanceHistory.get(i).date.get(Calendar.WEEK_OF_YEAR) == period.get(Calendar.WEEK_OF_YEAR) && !isMonth
                    || stats.attendanceHistory.get(i).date.get(Calendar.YEAR) == period.get(Calendar.YEAR)
                    && stats.attendanceHistory.get(i).date.get(Calendar.MONTH) == period.get(Calendar.MONTH) && isMonth) {
                float xValue = (float) getIndex(i, isMonth) + 1; //mon is 1
                float yValue = getYValue(stats.attendanceHistory.get(i).lessons);

                noData = false;
                filled[getIndex(i, isMonth)] = true;

                colors[getIndex(i, isMonth)] = ContextCompat.getColor(this, StatsUtils.getColorForYValue(yValue, shiftUpwards));
                entries.set(getIndex(i, isMonth), new BarEntry(xValue, yValue));

                Log.d(TAG, Integer.toString(i) + "th entry: " + Float.toString(xValue) + " " + Float.toString(yValue));
            }
        }

        for (int i = 0; i < length; i++) { //fill chart up to full length with zeroes
            if (!filled[i]) {
                float xValue = (float) i + 1;
                float yValue = 0.0f;
                //entries when no periods are not shifted -- no bars will be shown
                entries.set(i, new BarEntry(xValue, yValue));

                Log.d(TAG, Integer.toString(i) + "th entry: " + Float.toString(xValue) + " " + Float.toString(yValue));
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "attendanceBars");
        dataSet.setColors(colors);

        return new StatsUtils.GeneratedData<>(dataSet, noData);
    }

    /**
     * gets an array with booleans (i.e. day) and returns y-value for such array to display in barChart
     */
    private float getYValue(ArrayList<Boolean> arr) {
        int counter = 0;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i)) {
                counter++;
            }
        }
        if (arr.size() == 0) {
            return 0;
        }
        return (float) counter / (float) arr.size() + shiftUpwards;
    }
}
