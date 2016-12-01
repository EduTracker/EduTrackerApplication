package com.project.itmo2016.edutrackerapplication;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.project.itmo2016.edutrackerapplication.loader.LoadResult;
import com.project.itmo2016.edutrackerapplication.models.Schedule;

public class ChooseGroupActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<Schedule>> {

    public static final String TAG = "ChooseGroupActivity tag";

    //    static ChooseGroupActivity currentRunningInstance = null;
    FrameLayout loadingLayout;
    FrameLayout enterGroupLayout;
    EditText enterField;
    TextView errorText;
    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);

        loadingLayout = (FrameLayout) findViewById(R.id.loading_json_layout);
        enterGroupLayout = (FrameLayout) findViewById(R.id.enter_group_layout);
        enterField = (EditText) findViewById(R.id.group_edit_text);
        errorText = (TextView) findViewById(R.id.choose_group_error);
        okButton = (Button) findViewById(R.id.button_ok_choose_group);

        loadingLayout.setVisibility(View.VISIBLE);

        //TODO launch a loader to get JSON, parse it and display all the groups in Spinner
    }

    @Override
    public Loader<LoadResult<Schedule>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<Schedule>> loader, LoadResult<Schedule> data) {

    }

    @Override
    public void onLoaderReset(Loader<LoadResult<Schedule>> loader) {

    }

//    public static ChooseGroupActivity getInstance() {
//        return currentRunningInstance;
//    }
//
//    @Override
//    public void finish() {
//        setResult(-1, new Intent("tmp")); //TODO chosen group will be returned here
//        super.finish();
//    }
}
