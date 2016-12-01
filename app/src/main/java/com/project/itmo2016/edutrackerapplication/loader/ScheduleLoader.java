package com.project.itmo2016.edutrackerapplication.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.project.itmo2016.edutrackerapplication.ChooseGroupActivity;
import com.project.itmo2016.edutrackerapplication.models.Schedule;
import com.project.itmo2016.edutrackerapplication.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class ScheduleLoader extends AsyncTaskLoader<LoadResult<Schedule>> {

    private Schedule data = null;
    ResultType resultType = ResultType.ERROR;

    public ScheduleLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (data == null)
            forceLoad();
        else
            deliverResult(new LoadResult<Schedule>(resultType, data));
    }

//    private void parseResponse(InputStream in) throws Exception {
//        final JSONObject jsonObj = new JSONObject(IOUtils.readToString(in, "UTF-8"));
//        parseJson(jsonObj);
//    }

//    private void parseJson(JSONObject jsonObj) throws Exception {
//        data = new ArrayList<>();
//        JSONArray arr = jsonObj.getJSONArray("results");
//        for (int i = 0; i < arr.length(); i++) {
//            JSONObject movie = arr.optJSONObject(i);
//            if (movie == null)
//                continue;
//            data.add(new Movie(movie.optString("poster_path"),
//                    movie.optString("original_title"),
//                    movie.optString("overview"),
//                    movie.optString("title")));
//        }
//    }

    @Override
    public LoadResult<Schedule> loadInBackground() {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            connection = ITMOScheduleApi.getScheduleRequest();
            Log.d(ChooseGroupActivity.TAG, "Establishing connection: " + connection.getURL());

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();

                Log.d(ChooseGroupActivity.TAG, "Connection established, everything cool, starting parsing");
                IOUtils.readFully(in); //tmp, while parser isn't ready
//                parseResponse(in);

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
