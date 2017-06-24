package com.project.itmo2016.edutrackerapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.project.itmo2016.edutrackerapplication.input.Input;
import com.project.itmo2016.edutrackerapplication.loader.LoadResult;
import com.project.itmo2016.edutrackerapplication.loader.ResultType;
import com.project.itmo2016.edutrackerapplication.loader.ScheduleLoader;
import com.project.itmo2016.edutrackerapplication.models.schedule.GlobalSchedule;
import com.project.itmo2016.edutrackerapplication.models.schedule.LocalSchedule;

public class ChooseGroupActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<GlobalSchedule>> {

    public static final String TAG = "ChooseGroupActivity tag";
    private static final String KEY_EDIT_TEXT = "edit text";

    private FrameLayout loadingLayout;
    private FrameLayout enterGroupLayout;
    private EditText enterField;
    private TextView errorInputText;
    private TextView errorLoadingText;

    private GlobalSchedule globalSchedule = null;
    private LocalSchedule localSchedule = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);

        loadingLayout = (FrameLayout) findViewById(R.id.loading_json_layout);
        enterGroupLayout = (FrameLayout) findViewById(R.id.enter_group_layout);
        enterField = (EditText) findViewById(R.id.group_edit_text);
        errorInputText = (TextView) findViewById(R.id.choose_group_error);
        errorLoadingText = (TextView) findViewById(R.id.error_while_loading_text);
        Button okButton = (Button) findViewById(R.id.button_ok_choose_group);
        okButton.setOnClickListener(okListener);

        loadingLayout.setVisibility(View.VISIBLE);

        if (savedInstanceState != null) {
            loadFromSavedState(savedInstanceState);
        }

        Bundle b = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, b, this);
    }

    @Override
    public Loader<LoadResult<GlobalSchedule>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new ScheduleLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<GlobalSchedule>> loader, LoadResult<GlobalSchedule> data) {
        Log.d(TAG, "onLoadFinished");

        loadingLayout.setVisibility(View.INVISIBLE);

        if (data.resultType == ResultType.OK) {
            enterGroupLayout.setVisibility(View.VISIBLE);
            globalSchedule = data.data;
            Log.d(TAG, "globalSchedule defined");
        } else {
            Log.d(TAG, "resultType isn't Ok");
            errorLoadingText.setVisibility(View.VISIBLE);
            if (data.resultType == ResultType.NO_INTERNET) {
                errorLoadingText.setText(R.string.error_no_internet_connection);
            } else {
                errorLoadingText.setText(R.string.error_while_loading);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<GlobalSchedule>> loader) {
    }

    private View.OnClickListener okListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String groupName = enterField.getText().toString();

            if (groupName.equals("")) {
                groupName = getString(R.string.default_name_hint); //default group name
            }

            Log.d(TAG, "okButton pressed, groupName entered is: " + groupName);

            //check if there is schedule for such group in globalSchedule
            localSchedule = Input.processGroupName(globalSchedule, groupName);
            if (localSchedule != null) {
                Log.d(TAG, "group " + groupName + " found in global schedule, leaving chooseGroupActivity");
                exitActivity();
            } else {
                Log.d(TAG, "groupName " + groupName + " wasn't found in global schedule, displaying error msg");
                errorInputText.setVisibility(View.VISIBLE);
            }
        }
    };

    private void exitActivity() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("localSchedule", localSchedule);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onSaveInstanceState(new Bundle());
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(KEY_EDIT_TEXT, enterField.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void loadFromSavedState(Bundle savedInstanceState) {
        enterField.setText(savedInstanceState.getString(KEY_EDIT_TEXT));
    }
}
