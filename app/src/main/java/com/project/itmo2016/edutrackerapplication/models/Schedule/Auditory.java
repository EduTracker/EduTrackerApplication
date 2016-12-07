package com.project.itmo2016.edutrackerapplication.models.Schedule;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class Auditory implements Serializable {

    @NonNull
    public final String auditoryNumber;

    @NonNull
    public final String auditoryAddress;

    public Auditory(@NonNull String auditoryNumber, @NonNull String auditoryAddress) {
        this.auditoryNumber = auditoryNumber;
        this.auditoryAddress = auditoryAddress;
    }
}
