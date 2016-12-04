package com.project.itmo2016.edutrackerapplication.loader;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class ITMOScheduleApi {

//    private static final String API_KEY = "e58d2b7c6c07252f6aa52391f397b065";

    //tmp json is stored here
    private static final String BASE_URI = "https://api.myjson.com/bins/47o7r";

    public static HttpURLConnection getScheduleRequest() throws IOException {
        Uri uri = Uri.parse(BASE_URI);
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
