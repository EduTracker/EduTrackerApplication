package com.project.itmo2016.edutrackerapplication.cache;

import android.provider.BaseColumns;

class CheckboxDataContract {
    private CheckboxDataContract() {
    }

    interface CheckboxColumns extends BaseColumns {
        String DAY = "DAY";
        String LESSONNUMBER = "LESSONNUMBER";
        String VALUE = "VALUE";
    }

    static final class Checbox implements CheckboxColumns {
        static final String TABLE = "database";
        static final String CREATETABLE =
                "CREATE TABLE " + TABLE + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        DAY + " INTEGER, " +
                        LESSONNUMBER + " INTEGER, " +
                        VALUE + " INTEGER )";

        static final String[] columns = new String[]{DAY, LESSONNUMBER, VALUE};
    }
}
