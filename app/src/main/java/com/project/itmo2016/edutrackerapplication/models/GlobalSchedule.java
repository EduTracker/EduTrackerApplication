package com.project.itmo2016.edutrackerapplication.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */


/**
 * Class for storing schedule for whole university
 */
public class GlobalSchedule {

    @NonNull
    public final ArrayList<Faculty> faculties;

    public GlobalSchedule(@NonNull ArrayList<Faculty> faculties) {
        this.faculties = faculties;
    }
}
