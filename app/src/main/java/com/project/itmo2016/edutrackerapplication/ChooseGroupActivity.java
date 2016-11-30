package com.project.itmo2016.edutrackerapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

public class ChooseGroupActivity extends AppCompatActivity {

    Spinner chooseGroup;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);

        chooseGroup = (Spinner) findViewById(R.id.spinnerGroup);
        //TODO launch a loader to get JSON, parse it and display all the groups in Spinner
    }

    @Override
    public void finish() {
        setResult(-1, new Intent("tmp")); //TODO chosen group will be returned here
        super.finish();
    }
}
