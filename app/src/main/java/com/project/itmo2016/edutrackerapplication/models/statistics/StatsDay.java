package com.project.itmo2016.edutrackerapplication.models.statistics;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Aleksandr Tukallo on 07.12.16.
 */

public class StatsDay implements Serializable {
    @NonNull
    public final ArrayList<Boolean> lessons; //size of array equals number of lessons in schedule.

    @NonNull
    public final GregorianCalendar date; //stores day number, month and year. Weekday can easily be received.

    public StatsDay(@NonNull ArrayList<Boolean> lessons, @NonNull GregorianCalendar date) {
        this.lessons = lessons;
        this.date = date;
    }
}
