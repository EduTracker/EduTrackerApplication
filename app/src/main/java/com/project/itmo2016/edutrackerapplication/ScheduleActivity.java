package com.project.itmo2016.edutrackerapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.project.itmo2016.edutrackerapplication.cache.CheckboxCache;
import com.project.itmo2016.edutrackerapplication.loader.WeekRecycleAdapter;
import com.project.itmo2016.edutrackerapplication.models.schedule.LocalSchedule;
import com.project.itmo2016.edutrackerapplication.models.statistics.Stats;
import com.project.itmo2016.edutrackerapplication.models.statistics.StatsDay;
import com.project.itmo2016.edutrackerapplication.utils.FileIOUtils;
import com.project.itmo2016.edutrackerapplication.utils.RecylcerDividersDecorator;
import com.project.itmo2016.edutrackerapplication.utils.StatsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ScheduleActivity extends Drawer {

    public static final String TAG = "ScheduleActivity tag";
    public static final String EXTRA_PATH_TO_STATS = "extraPathToStats";
    public static final int NUMBER_OF_DAYS_IN_WEEK = 7;
    public static final int MAX_PAIRS_AMOUNT_PER_DAY = 10;
    private static final int REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY = 1;
    private static final String PATH_TO_LOCAL_SCHEDULE = "localSchedule";
    private static final String PATH_TO_LATEST_DATE = "latestEnter";
    private static final String BASE_FOR_PATH_TO_STATS = "stats";

    GregorianCalendar latestEnter;
    TextView error;
    RecyclerView recyclerView;
    WeekRecycleAdapter adapter = null;
    private String pathToStats; // = base + groupName
    private LocalSchedule localSchedule = null;

    /**
     * localSchedule and pathToStats are initialized here
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_schedule, null, false);
        drawer.addView(contentView, 0);

        recyclerView = (RecyclerView) findViewById(R.id.scheduleRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(ContextCompat.getColor(this, R.color.gray_a)));

        error = (TextView) findViewById(R.id.error_text);
        error.setVisibility(View.VISIBLE);


        //Loading schedule from another activity if needed
        if (!isFileCreated(PATH_TO_LOCAL_SCHEDULE)) {
            Log.d(TAG, "localSchedule must be downloaded");

            startActivityForResult(new Intent(this, ChooseGroupActivity.class), REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY);
        } else {
            Log.d(TAG, "localSchedule was already downloaded, loading it from file");

            localSchedule = FileIOUtils.loadSerializableFromFile(PATH_TO_LOCAL_SCHEDULE, this);
            assert localSchedule != null;
            pathToStats = BASE_FOR_PATH_TO_STATS + localSchedule.groupName;
            displaySchedule(localSchedule);

            //If app was opened last time more than a week ago, checkboxes must be saved to stats and turned to false
            latestEnter = FileIOUtils.loadSerializableFromFile(PATH_TO_LATEST_DATE, this);
            FileIOUtils.saveObjectToFile(StatsUtils.ensureNotSunday(new GregorianCalendar()), PATH_TO_LATEST_DATE, this);
            saveCheckBoxesToStats(StatsUtils.ensureNotSunday(latestEnter));
            //TODO make all checkboxes false here
        }

    }

    private ArrayList<ArrayList<Integer>> getCheckbox() {
        ArrayList<ArrayList<Integer>> checkboxData = new ArrayList<>();
        CheckboxCache table = new CheckboxCache(this);
        for (int i = 0; i < NUMBER_OF_DAYS_IN_WEEK; i++) {
            checkboxData.add(new ArrayList<Integer>());
            for (int j = 0; j < MAX_PAIRS_AMOUNT_PER_DAY; j++) {
                checkboxData.get(i).add(table.get(i, j));
            }
        }

        return checkboxData;
    }


    /**
     * Method returns array with booleans for each period (visited\missed) for specified day.
     *
     * @param weekDay is the day to get array of attendance for. Important! weekDay's numeration is the same as
     *                numeration of days in GregorianCalendar (Monday is 2, Saturday is 7).
     * @return has size of number of periods on weekDay. Every period is either visited or missed
     */
    private ArrayList<Boolean> getArrOfAttendance(int weekDay) {
        ArrayList<Boolean> ret = new ArrayList<>();
        ArrayList<ArrayList<Integer>> checkbox = getCheckbox();
        for (int i = 0; i < localSchedule.days.size(); i++) {
            if (localSchedule.days.get(i).dayOfTheWeek == StatsUtils.convertWeekDayNumeration(weekDay) + 1) {
                for (int j = 0; j < localSchedule.days.get(i).lessons.size(); j++) {
                    ret.add(checkbox.get(StatsUtils.convertWeekDayNumeration(weekDay)).get(j) == 1);
                }
                break;
            }
        }
        return ret; //if not found, 0 periods in that day, empty array, it's ok
    }

    /**
     * @param date is not Sunday!
     */
    private void saveCheckBoxesToStats(GregorianCalendar date) {
        //load stats from file
        Stats stats = FileIOUtils.loadSerializableFromFile(pathToStats, this);

        boolean found = false; //if one day is found, all days of the week before date are found.
        for (int i = 0; i < stats.attendanceHistory.size(); i++) {
            if (stats.attendanceHistory.get(i).date.get(Calendar.WEEK_OF_YEAR) == date.get(Calendar.WEEK_OF_YEAR)
                    && stats.attendanceHistory.get(i).date.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                    && stats.attendanceHistory.get(i).date.get(Calendar.DAY_OF_WEEK) <= date.get(Calendar.DAY_OF_WEEK)) {
                found = true;
                //arr updated according to checkboxes, they override previous data for current week
                stats.attendanceHistory.set(i,
                        new StatsDay(
                                getArrOfAttendance(stats.attendanceHistory.get(i).date.get(Calendar.DAY_OF_WEEK)),
                                stats.attendanceHistory.get(i).date));
            }
        }

        //if date is not found in stats, we need to add all of the week date belongs to to stats
        if (!found) {
            GregorianCalendar itDate = StatsUtils.copyCalendarConstructor(date);
            StatsUtils.moveToMonday(itDate);

            //fill stats with data from checkboxes before date inclusively
            while (StatsUtils.convertWeekDayNumeration(itDate.get(Calendar.DAY_OF_WEEK))
                    <= StatsUtils.convertWeekDayNumeration(date.get(Calendar.DAY_OF_WEEK))) {

                stats.attendanceHistory.add(new StatsDay(getArrOfAttendance(itDate.get(Calendar.DAY_OF_WEEK)),
                        StatsUtils.copyCalendarConstructor(itDate)));

                itDate.add(Calendar.DAY_OF_WEEK, 1);
            }

            //fill with empty arrs till end of the week:
            while (StatsUtils.convertWeekDayNumeration(itDate.get(Calendar.DAY_OF_WEEK))
                    <= StatsUtils.convertWeekDayNumeration(Calendar.SATURDAY)) {

                stats.attendanceHistory.add(new StatsDay(new ArrayList<Boolean>(),
                        StatsUtils.copyCalendarConstructor(itDate)));

                itDate.add(Calendar.DAY_OF_WEEK, 1);
            }
        }

        //saving changed file back
        FileIOUtils.saveObjectToFile(stats, pathToStats, this);

        Log.d(TAG, "Checkboxes updated");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.stats) {
            //starting statistics activity
            saveCheckBoxesToStats(new GregorianCalendar());

            final Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
            intent.putExtra(EXTRA_PATH_TO_STATS, pathToStats);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySchedule(LocalSchedule localSchedule) {
        assert localSchedule != null;
        Log.d("Trying to display", String.valueOf(localSchedule.days.size()));
        if (adapter == null) {
            adapter = new WeekRecycleAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setSchedule(localSchedule);
        error.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        Log.d("adapter size", String.valueOf(adapter.getItemCount()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY): {
                Log.d(TAG, "got result from Choose Group Activity");
                if (resultCode == Activity.RESULT_OK) {
                    onLocalScheduleReturned(data);
                }
                break;
            }
        }
    }

    private void onLocalScheduleReturned(Intent data) {
        localSchedule = (LocalSchedule) data.getSerializableExtra("localSchedule");

        Log.d(TAG, "save local schedule to file");

        FileIOUtils.saveObjectToFile(localSchedule, PATH_TO_LOCAL_SCHEDULE, this);
        pathToStats = BASE_FOR_PATH_TO_STATS + localSchedule.groupName;

        Log.d(TAG, "save new stats to file");

        Stats newStats = new Stats(new ArrayList<StatsDay>(), localSchedule.groupName);
        FileIOUtils.saveObjectToFile(newStats, pathToStats, this);

        latestEnter = new GregorianCalendar();
        FileIOUtils.saveObjectToFile(latestEnter, PATH_TO_LATEST_DATE, this);

        error.setVisibility(View.GONE);
        displaySchedule(localSchedule);
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    private boolean isFileCreated(String PATH) {
        File f = new File(getFilesDir(), PATH);
        return f.exists();
    }
}
