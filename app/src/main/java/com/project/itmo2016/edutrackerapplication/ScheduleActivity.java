package com.project.itmo2016.edutrackerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Aleksandr Tukallo on 30.11.16.
 */
public class ScheduleActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO add "if not loaded already"
        startActivity(new Intent(this, ChooseGroupActivity.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_schedule);
    }
}
