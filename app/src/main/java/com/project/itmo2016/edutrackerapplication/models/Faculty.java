package com.project.itmo2016.edutrackerapplication.models;

import android.support.annotation.NonNull;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */
public class Faculty {

    @NonNull
    public final Group[] groups;

    @NonNull
    public final String facultyName;

    public Faculty(@NonNull Group[] groups, @NonNull String facultyName) {
        this.groups = groups;
        this.facultyName = facultyName;
    }
}
