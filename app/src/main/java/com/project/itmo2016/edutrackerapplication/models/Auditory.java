package com.project.itmo2016.edutrackerapplication.models;

import android.support.annotation.NonNull;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class Auditory {

    @NonNull
    public final String auditoryNumber;

    @NonNull
    public final String auditoryAddress;

    public Auditory(@NonNull String auditoryNumber, @NonNull String auditoryAddress) {
        this.auditoryNumber = auditoryNumber;
        this.auditoryAddress = auditoryAddress;
    }
}
