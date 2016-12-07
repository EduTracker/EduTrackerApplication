package com.project.itmo2016.edutrackerapplication.models.Statistics;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aleksandr Tukallo on 07.12.16.
 */

public class StatsDay {
    @NonNull
    public final ArrayList<Boolean> lessons; //size of array equals number of lessons in schedule.

    @NonNull
    public final Date date; //stores day number, month and year. Weekday can easily be received.

    public StatsDay(@NonNull ArrayList<Boolean> lessons, @NonNull Date date) {
        this.lessons = lessons;
        this.date = date;
    }
}
