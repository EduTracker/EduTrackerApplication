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

import com.project.itmo2016.edutrackerapplication.loader.WeekRecycleAdapter;
import com.project.itmo2016.edutrackerapplication.models.schedule.LocalSchedule;
import com.project.itmo2016.edutrackerapplication.models.statistics.Stats;
import com.project.itmo2016.edutrackerapplication.models.statistics.StatsDay;
import com.project.itmo2016.edutrackerapplication.utils.FileIOUtils;
import com.project.itmo2016.edutrackerapplication.utils.RecylcerDividersDecorator;

import java.io.File;
import java.util.ArrayList;

public class ScheduleActivity extends Drawer {

    private static final int REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY = 1;
    public static final String TAG = "ScheduleActivity tag";
    private static final String PATH_TO_LOCAL_SCHEDULE = "localSchedule";
    private static final String BASE_FOR_PATH_TO_STATS = "stats";
    public static final String EXTRA_PATH_TO_STATS = "extraPathToStats";
    private String pathToStats; // = base + groupName

    private final int NUMBEROFDAYSINWEEK = 7;
    private final int MAXPAIRSAMOUNTPERDAY = 10;

    private ArrayList<ArrayList<Boolean>> checkboxData; // checkboxData[weekDay = {0..6}][{pairNumber = {0, ?9}] = true
                                                        // if lesson pairNumberth lesson on {"Monday", .., "Sunday"}.get(weekDay) was attended
                                                        // false - otherwise.

    private LocalSchedule localSchedule = null;

    TextView error;
    RecyclerView recyclerView;
    WeekRecycleAdapter adapter = null;

    /**
     * localSchedule and pathToStats are initialized here
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_schedule, null, false);
        drawer.addView(contentView ,0);

        recyclerView = (RecyclerView) findViewById(R.id.scheduleRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(ContextCompat.getColor(this, R.color.gray_a)));

        error = (TextView) findViewById(R.id.error_text);
        error.setVisibility(View.VISIBLE);

//      TODO: Need to store checkboxData in DataBase.
/*          if (checkboxData is in DataBase)
            // TODO: Get checkboxData from DataBase.
        else
 */
        checkboxData = new ArrayList<>();
        for (int i = 0; i < NUMBEROFDAYSINWEEK; i++) {
            checkboxData.add(new ArrayList<Boolean>());
            for (int j = 0; j < MAXPAIRSAMOUNTPERDAY; j++) {
                checkboxData.get(i).add(false);
            }
        }

//        if (!isLocalScheduleDownloaded() || true) { //true here only for debug
        if (!isLocalScheduleDownloaded()) {
            Log.d(TAG, "localSchedule must be downloaded");

            startActivityForResult(new Intent(this, ChooseGroupActivity.class), REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY);
        } else {
            Log.d(TAG, "localSchedule was already downloaded, loading it from file");

            localSchedule = FileIOUtils.loadSerializableFromFile(PATH_TO_LOCAL_SCHEDULE, this);
            assert localSchedule != null;
            pathToStats = BASE_FOR_PATH_TO_STATS + localSchedule.groupName;
            displaySchedule(localSchedule);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.stats) {
            //starting statistics activity
            final Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
            intent.putExtra(EXTRA_PATH_TO_STATS, pathToStats);
            startActivity(intent);
        }

        /*if (id == R.id.schedule) {
            final Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
            startActivity(intent);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySchedule(LocalSchedule localSchedule) {
        assert localSchedule != null;
        Log.d("Trying to display", String.valueOf(localSchedule.days.size()));
        if (adapter == null) {
            adapter = new WeekRecycleAdapter(this, checkboxData);
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

        //TODO the idea is that once in a period stats will be extracted from file, updated according
        //     to attendence\miss of the pair and written back to the file.

        Log.d(TAG, "save new stats to file");

        Stats newStats = new Stats(new ArrayList<StatsDay>(), localSchedule.groupName);
        FileIOUtils.saveObjectToFile(newStats, pathToStats, this);

        error.setVisibility(View.GONE);
        displaySchedule(localSchedule);
    }

    private boolean isLocalScheduleDownloaded() {
        File f = new File(getFilesDir(), PATH_TO_LOCAL_SCHEDULE);
        return f.exists();
    }
}
