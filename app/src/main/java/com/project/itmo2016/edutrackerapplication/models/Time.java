package com.project.itmo2016.edutrackerapplication.models;

import android.support.annotation.NonNull;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class Time {
    @NonNull
    public final int hour;

    @NonNull
    public final int minute;

    public String toString() {
        return Integer.toString(hour) + ":" + Integer.toString(minute);
    }

    public Time(@NonNull int hour, @NonNull int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time(@NonNull String time) {
        String[] str = time.split(":");
        hour = Integer.parseInt(str[0]);
        minute = Integer.parseInt(str[1]);
    }
}
