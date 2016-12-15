package com.project.itmo2016.edutrackerapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

/**
 * Created by Aleksandr Tukallo on 30.11.16.
 */
public class ScheduleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY = 1;
    public static final String TAG = "ScheduleActivity tag";
    private static final String PATH_TO_LOCAL_SCHEDULE = "localSchedule";
    private static final String BASE_FOR_PATH_TO_STATS = "stats";
    public static final String EXTRA_PATH_TO_STATS = "extraPathToStats";
    private String pathToStats; // = base + groupName

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
        setContentView(R.layout.activity_main_schedule);

//        if (!isLocalScheduleDownloaded() || true) { //TODO true here only for debug
        if (!isLocalScheduleDownloaded()) {
            Log.d(TAG, "localSchedule must be downloaded");

            startActivityForResult(new Intent(this, ChooseGroupActivity.class), REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY);
        } else {
            Log.d(TAG, "localSchedule was already downloaded, loading it from file");

            localSchedule = FileIOUtils.loadSerializableFromFile(PATH_TO_LOCAL_SCHEDULE, this);
            pathToStats = BASE_FOR_PATH_TO_STATS + localSchedule.groupName;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "По нажатию этой кнопки можно будет поделиться статистикой VK"
                        , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.scheduleRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(ContextCompat.getColor(this, R.color.gray_a)));

        error = (TextView) findViewById(R.id.error_text);
        displaySchedule(localSchedule);
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
//        displayError();
    }

    private void displayError() {
        error.setText("Расписание недоступно");
        error.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
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

        displaySchedule(localSchedule);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.schedule) {
            //TODO переход на активити с расписаием
        } else if (id == R.id.stats) {
            //starting statistics activity
            final Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
            intent.putExtra(EXTRA_PATH_TO_STATS, pathToStats);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isLocalScheduleDownloaded() {
        File f = new File(getFilesDir(), PATH_TO_LOCAL_SCHEDULE);
        return f.exists();
    }
}
