package com.project.itmo2016.edutrackerapplication.models.statistics;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aleksandr Tukallo on 07.12.16.
 */

/**
 * It is a main class to store statistics of attendance. It's fields are:
 * 1) a groupName. It is needed to uniquely identify statistics, because student may change
 * his groupName in settings over time. When groupName changed, new Stats object is created. When changed back,
 * we restore previous statistics from memory by its groupName
 * 2) an array of StatsDay. Each StatsDay is one of 6 days of the week. StatsDay stores information about
 * visited and missed periods. There are as many elements in StatsDay array, as many days have passed (except Sunday),
 * since this groupName is chosen.
 */
public class Stats implements Serializable {
    @NonNull
    public final ArrayList<StatsDay> attendanceHistory;

    @NonNull
    public final String groupName;

    public Stats(ArrayList<StatsDay> attendanceHistory, @NonNull String groupName) {
        this.attendanceHistory = attendanceHistory;
        this.groupName = groupName;
    }
}
