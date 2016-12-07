package com.project.itmo2016.edutrackerapplication.models.Statistics;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Aleksandr Tukallo on 07.12.16.
 */

public class Stats {
    @NonNull
    public final ArrayList<StatsDay> attendanceHistory;

    @NonNull
    public final String groupName;

    public Stats(ArrayList<StatsDay> attendanceHistory, @NonNull String groupName) {
        this.attendanceHistory = attendanceHistory;
        this.groupName = groupName;
    }
}
