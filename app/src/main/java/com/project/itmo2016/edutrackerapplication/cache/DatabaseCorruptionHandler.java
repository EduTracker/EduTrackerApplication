package com.project.itmo2016.edutrackerapplication.cache;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

class DatabaseCorruptionHandler implements DatabaseErrorHandler {
    private static final String TAG = "Database corruption";
    private final Context c;
    private final String name;

    DatabaseCorruptionHandler(Context c, String name) {
        this.c = c.getApplicationContext();
        this.name = name;
    }

    @Override
    public void onCorruption(SQLiteDatabase sqLiteDatabase) {
        final boolean isCorrupted = !sqLiteDatabase.isDatabaseIntegrityOk();

        try {
            sqLiteDatabase.close();
        } catch (SQLException ignored) {
        }

        final File dbFile = c.getDatabasePath(name);
        if (isCorrupted) {
            Log.d(TAG, "Database " + name + " is not corrupted");
        } else {
            Log.d(TAG, "Database " + name + " is corrupted");
            dbFile.delete();
        }

    }
}
