package com.project.itmo2016.edutrackerapplication.models.schedule;

/**
 * Created by Aleksandr Tukallo on 04.12.16.
 */

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for storing schedule for only one group.
 * It implements Serializable, because object of this class is returned from ChooseGroupActivity
 * as an Extra field in Intent, so it must be Serializable\Parcelable.
 */
public class LocalSchedule implements Serializable {

    @NonNull
    public final String facultyName;

    @NonNull
    public final String groupName;

    @NonNull
    public final ArrayList<Day> days;

    //mb also need to store when was loaded last time

    public LocalSchedule(@NonNull String facultyName, @NonNull String groupName, @NonNull ArrayList<Day> days) {
        this.facultyName = facultyName;
        this.groupName = groupName;
        this.days = days;
    }
}
