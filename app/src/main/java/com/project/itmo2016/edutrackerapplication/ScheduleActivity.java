package com.project.itmo2016.edutrackerapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.project.itmo2016.edutrackerapplication.models.LocalSchedule;

/**
 * Created by Aleksandr Tukallo on 30.11.16.
 */
public class ScheduleActivity extends AppCompatActivity {

    final int REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY = 1;
    final String TAG = "Schedule Activity";

    LocalSchedule localSchedule = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO add "if not loaded already"
        startActivityForResult(new Intent(this, ChooseGroupActivity.class), REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_schedule);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE_FOR_CHOOSE_GROUP_ACTIVITY): {
                Log.d(TAG, "got result from Choose Group Activity");
                if (resultCode == Activity.RESULT_OK) {
                    localSchedule = (LocalSchedule) data.getSerializableExtra("localSchedule");
                }
                break;
            }
        }
    }
}
