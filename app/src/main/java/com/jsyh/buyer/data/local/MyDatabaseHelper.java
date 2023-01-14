package com.jsyh.buyer.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mo on 17-4-18.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    String sql_create = "create table " + DatabaseConstance.TAB_SEARCH + " (id integer primary key autoincrement,name text)";
    public static final String drop_search = "drop table if exists "+DatabaseConstance.TAB_SEARCH;

    String msg_sql_create = "create table " + DatabaseConstance.TAB_MESSAGE + " (id integer primary key autoincrement,time text,content text,read integer)";
    public static final String drop_msg = "drop table if exists "+DatabaseConstance.TAB_MESSAGE;

    public MyDatabaseHelper(Context context) {
        super(context, DatabaseConstance.BATA_NAME, null, DatabaseConstance.VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_create);
        db.execSQL(msg_sql_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop_search);
        db.execSQL(drop_msg);

        db.execSQL(sql_create);
        db.execSQL(msg_sql_create);

        db.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
