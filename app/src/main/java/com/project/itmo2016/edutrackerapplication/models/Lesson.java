package com.project.itmo2016.edutrackerapplication.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class Lesson implements Serializable {

    @NonNull
    public final String subject;

    @NonNull
    public final Time startTime;

    @NonNull
    public final Time endTime;

//    @Retention(RetentionPolicy.SOURCE)
//    @IntDef({0, 1, 2})
//    public @interface Parity {
//    }

    /**
     * parity can have 3 values:
     * if parity is 0, the lesson is held every week
     * if parity is 1, the lesson is held on odd weeks
     * if parity is 2, the lesson is held on even weeks
     */
    @NonNull
//    @Parity
    public final int parity;

//    @NonNull
//    public final String teachers; //don't nees it currently
    //Also do not need type, date_start, date_end

    /**
     * Auditory can be unfortunately null
     */
    public final ArrayList<Auditory> auditories;

    public Lesson(@NonNull String subject, @NonNull Time startTime, @NonNull Time endTime, @NonNull /*@Parity*/ int parity, ArrayList<Auditory> auditories) {
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.parity = parity;
        this.auditories = auditories;
    }
}
