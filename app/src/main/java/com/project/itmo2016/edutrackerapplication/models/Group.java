package com.project.itmo2016.edutrackerapplication.models;

import android.support.annotation.NonNull;

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
    public final Day[] days;

    public Group(@NonNull String groupName, @NonNull Day[] days) {
        this.groupName = groupName;
        this.days = days;
    }
}
