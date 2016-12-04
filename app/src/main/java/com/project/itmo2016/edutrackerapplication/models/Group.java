package com.project.itmo2016.edutrackerapplication.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class Group {

    /**
     * Name of a group
     * e.g.: "M3238"
     */
    @NonNull
    public final String groupName;

    @NonNull
    public final ArrayList<Day> days;

    public Group(@NonNull ArrayList<Day> days, @NonNull String groupName) {
        this.groupName = groupName;
        this.days = days;
    }
}
