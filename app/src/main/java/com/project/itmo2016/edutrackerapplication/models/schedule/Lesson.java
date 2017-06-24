package com.project.itmo2016.edutrackerapplication.models.schedule;

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

    /**
     * parity can have 3 values:
     * if parity is 0, the lesson is held every week
     * if parity is 1, the lesson is held on odd weeks
     * if parity is 2, the lesson is held on even weeks
     */
    @NonNull
    public final int parity;

    /**
     * Auditory can be null, if it is null in JSON parsed, which happens.
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
