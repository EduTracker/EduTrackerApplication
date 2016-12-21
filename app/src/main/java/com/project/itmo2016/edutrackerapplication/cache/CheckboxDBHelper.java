package com.project.itmo2016.edutrackerapplication.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.project.itmo2016.edutrackerapplication.cache.CheckboxDataContract.Checbox.CREATETABLE;

class CheckboxDBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "checkbox.db";
    private static final int VERSION = 10;
    private static final String TAG = "CheckboxDBHelper";

    private static volatile CheckboxDBHelper instance = null;

    private CheckboxDBHelper(Context context) {
        super(context, DBNAME, null, VERSION, new DatabaseCorruptionHandler(context, DBNAME));
    }

    static CheckboxDBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (CheckboxDBHelper.class) {
                if (instance == null) {
                    instance = new CheckboxDBHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("Creating Table", CREATETABLE);
        sqLiteDatabase.execSQL(CREATETABLE);
        instance = this;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "Shouldn't happen yet. Surprise");
    }
}
