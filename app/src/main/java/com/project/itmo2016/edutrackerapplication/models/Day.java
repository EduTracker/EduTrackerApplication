package com.project.itmo2016.edutrackerapplication.models;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class Day {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({1, 2, 3, 4, 5, 6})
    public @interface DaysOfTheWeek {
    }

    @NonNull
    @Day.DaysOfTheWeek
    public final int dayOfTheWeek;

    @NonNull
    public final Lesson[] lessons;

    public Day(@NonNull @DaysOfTheWeek int dayOfTheWeek, @NonNull Lesson[] lessons) {
        this.dayOfTheWeek = dayOfTheWeek; //constructor is not convenient, it is a sacrifice for lessons being final
        this.lessons = lessons;
    }
}
