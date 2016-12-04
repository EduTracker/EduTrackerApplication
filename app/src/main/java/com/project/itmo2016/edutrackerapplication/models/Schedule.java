package com.project.itmo2016.edutrackerapplication.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */



public class Schedule {

    @NonNull
    public final ArrayList<Faculty> faculties;

    public Schedule(@NonNull ArrayList<Faculty> faculties) {
        this.faculties = faculties;
    }

    //mb also need to store when was loaded last time
}
