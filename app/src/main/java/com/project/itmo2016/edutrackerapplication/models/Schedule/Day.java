package com.project.itmo2016.edutrackerapplication.models.Schedule;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class Day implements Serializable {

//    @Retention(RetentionPolicy.SOURCE)
//    @IntDef({1, 2, 3, 4, 5, 6})
//    public @interface DaysOfTheWeek {
//    }

    /**
     * Is an int from diapason: [1;6]
     */
    @NonNull
//    @Day.DaysOfTheWeek
    public final int dayOfTheWeek;

    @NonNull
    public final ArrayList<Lesson> lessons;

    public Day(@NonNull ArrayList<Lesson> lessons, @NonNull /*@DaysOfTheWeek*/ int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.lessons = lessons;
    }
}
