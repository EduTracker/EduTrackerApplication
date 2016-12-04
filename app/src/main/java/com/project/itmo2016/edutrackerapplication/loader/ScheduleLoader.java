package com.project.itmo2016.edutrackerapplication.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.project.itmo2016.edutrackerapplication.ChooseGroupActivity;
import com.project.itmo2016.edutrackerapplication.models.GlobalSchedule;
import com.project.itmo2016.edutrackerapplication.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class ScheduleLoader extends AsyncTaskLoader<LoadResult<GlobalSchedule>> {

    private GlobalSchedule data = null;
    ResultType resultType = ResultType.ERROR;

    public ScheduleLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (data == null)
            forceLoad();
        else
            deliverResult(new LoadResult<GlobalSchedule>(resultType, data));
    }

    @Override
    public LoadResult<GlobalSchedule> loadInBackground() {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            connection = ITMOScheduleApi.getScheduleRequest();
            Log.d(ChooseGroupActivity.TAG, "Establishing connection: " + connection.getURL());

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();

                Log.d(ChooseGroupActivity.TAG, "Connection established, everything cool, starting parsing");

                data = ParserJSON.parseResponse(in); //parsing and saving a schedule

                resultType = ResultType.OK;
            } else {
                // consider all other codes as errors
                Log.d(ChooseGroupActivity.TAG, "Connection response code isn't ok, throwing exception");
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            try {
                if (IOUtils.isConnectionAvailable(getContext(), false)) {
                    resultType = ResultType.ERROR;
                } else {
                    resultType = ResultType.NO_INTERNET;
                }
            } catch (Exception f) {
                f.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null)
                connection.disconnect();
        }
        return new LoadResult<>(resultType, data);
    }
}
