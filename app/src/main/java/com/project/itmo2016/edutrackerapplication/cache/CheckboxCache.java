package com.project.itmo2016.edutrackerapplication.cache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import static com.project.itmo2016.edutrackerapplication.cache.CheckboxDataContract.Checbox.TABLE;
import static com.project.itmo2016.edutrackerapplication.cache.CheckboxDataContract.Checbox.columns;
import static com.project.itmo2016.edutrackerapplication.cache.CheckboxDataContract.CheckboxColumns.DAY;
import static com.project.itmo2016.edutrackerapplication.cache.CheckboxDataContract.CheckboxColumns.LESSONNUMBER;

public class CheckboxCache {

    @NonNull
    private final Context context;


    @AnyThread
    public CheckboxCache(@NonNull Context context) {
        this.context = context;
    }


    @WorkerThread
    public int get(int x, int y) {

        SQLiteDatabase db = CheckboxDBHelper.getInstance(context).getReadableDatabase();
        Cursor c = null;

        try {
            String pr = DAY + "=? AND " + LESSONNUMBER + "=?";
            String args[] = new String[]{String.valueOf(x), String.valueOf(y)};
//            System.out.println("pr=" + pr);
//            for (String arg : args) {
//                System.out.print(arg + " ");
//            }
//            System.out.println("CheckboxCache.get");
            c = db.query(TABLE, columns, pr, args, null, null, null);
            if (c != null && c.moveToFirst()) {
                int res = 0;
                while (!c.isAfterLast()) {
//                    assert c.getInt()
                    res = c.getInt(2);
                    c.moveToNext();
                }
                return res;
            } else {
//                System.out.println("CheckboxCache.get" + " null or !moved " + (c == null) + c.getPosition() + " " + c.getColumnNames());
                return 0;
            }
        } catch (SQLiteException ignored) {
            Log.d("SQLiteException", "ignored");
            return 0;
        } finally {
            if (c != null)
                c.close();
            db.close();
        }
    }

    @WorkerThread
    public void put(int x, int y, int val) {
        SQLiteDatabase db = CheckboxDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        SQLiteStatement in = null;

        try {
            String statement = "INSERT INTO " + TABLE + " (";
            String requestType = ") VALUES(" + x + ", " + y + ", ?";
            for (int i = 0; i < columns.length; i++) {
                statement += columns[i];
                if (i != columns.length - 1) {
                    statement += ", ";
                }
            }

            in = db.compileStatement(statement + requestType + ")");
            in.bindLong(1, val);
            in.executeInsert();
            db.setTransactionSuccessful();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception ignored) {
            }

            db.endTransaction();
        }
        db.close();
    }


}
