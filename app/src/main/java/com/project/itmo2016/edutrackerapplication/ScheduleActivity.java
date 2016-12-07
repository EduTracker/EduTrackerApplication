package com.project.itmo2016.edutrackerapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.project.itmo2016.edutrackerapplication.models.LocalSchedule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by Aleksandr Tukallo on 30.11.16.
 */
public class ScheduleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final int REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY = 1;
    final String TAG = "ScheduleActivity tag";
    final String PATH_TO_LOCAL_SCHEDULE = "localSchedule.txt";

    LocalSchedule localSchedule = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        if (!isLocalScheduleDownloaded()) {
            Log.d(TAG, "localSchedule must be downloaded");
            startActivityForResult(new Intent(this, ChooseGroupActivity.class), REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY);
        } else {
            Log.d(TAG, "localSchedule was already downloaded, no need to leave ScheduleActivity");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_schedule);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY): {
                Log.d(TAG, "got result from Choose Group Activity");
                if (resultCode == Activity.RESULT_OK) {
                    localSchedule = (LocalSchedule) data.getSerializableExtra("localSchedule");
                    saveLocalScheduleToFile();
                }
                break;
            }
        }
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
            //TODO переход на активити со статистикой
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isLocalScheduleDownloaded() {
        File f = new File(getFilesDir(), PATH_TO_LOCAL_SCHEDULE);
        return f.exists();
    }

    private void saveLocalScheduleToFile() {
        Log.d(TAG, "saving local schedule to file");
        File f = new File(getFilesDir(), PATH_TO_LOCAL_SCHEDULE);
        try {
            //getting byte array from serializable localSchedule, then saving byte arr to file f
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;

            out = new ObjectOutputStream(bos);
            out.writeObject(localSchedule);
            out.flush();

            FileOutputStream fout = new FileOutputStream(f);
            fout.write(bos.toByteArray());

            bos.close();
        } catch (Exception e) {
            Log.d(TAG, "exception when saving local schedule to file");
            e.printStackTrace();
            //noinspection ResultOfMethodCallIgnored
            f.delete();
        }
    }
}
