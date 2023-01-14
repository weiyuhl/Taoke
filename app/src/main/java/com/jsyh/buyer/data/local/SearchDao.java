package com.jsyh.buyer.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 17-4-18.
 */

public class SearchDao {


    private MyDatabaseHelper mHelper;

    public SearchDao(Context context) {
        mHelper = new MyDatabaseHelper(context);
    }


    public long insert(String name) {
        long insert = 0;
        try {

            deletebyName(name);


            SQLiteDatabase db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);

            insert = db.insert(DatabaseConstance.TAB_SEARCH, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return insert;
    }

    public List<String> getAll() {
        List<String> data = null;
        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();

            String sql = "select * from " + DatabaseConstance.TAB_SEARCH + " where 1=1 order by id desc";
            Cursor cursor = db.rawQuery(sql, null);
            data = new ArrayList<>();

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                data.add(name);
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return data;
    }

    public void clearAll() {

        try {
            SQLiteDatabase db = mHelper.getWritableDatabase();

            db.delete(DatabaseConstance.TAB_SEARCH, null, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletebyName(String name) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConstance.TAB_SEARCH, null, "name=?", new String[]{name}, null, null, null);
        while (cursor.moveToNext()) {
            deletByNameWithId(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")));
        }

    }


    public int deletByNameWithId(int id, String name) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        int delete = db.delete(DatabaseConstance.TAB_SEARCH, "id=? and name=?", new String[]{id + "", name});
        return delete;
    }


}
