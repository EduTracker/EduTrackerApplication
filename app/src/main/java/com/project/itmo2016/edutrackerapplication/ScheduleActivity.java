package com.project.itmo2016.edutrackerapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.project.itmo2016.edutrackerapplication.models.LocalSchedule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by Aleksandr Tukallo on 30.11.16.
 */
public class ScheduleActivity extends AppCompatActivity {

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
            f.delete();
        }
    }
}
