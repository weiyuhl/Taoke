package com.jsyh.buyer.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jsyh.pushlibrary.MessageModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mo on 17-4-19.
 */

public class MsgDao {

    private MyDatabaseHelper helper;

    public MsgDao(Context context) {
        helper = new MyDatabaseHelper(context);
    }


    public int deletAll() {
        int  delete = -1;
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
             delete = db.delete(DatabaseConstance.TAB_MESSAGE, null, null);

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return delete;
    }


    public List<MessageModel> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseConstance.TAB_MESSAGE, null, null, null, null, null, "id desc");

        List<MessageModel> data = new ArrayList<>();

        while (cursor.moveToNext()) {
            MessageModel model = new MessageModel(cursor.getString(cursor.getColumnIndex("time"))
                    , cursor.getString(cursor.getColumnIndex("content"))
                    , cursor.getInt(cursor.getColumnIndex("read")));
            model.setId(cursor.getInt(cursor.getColumnIndex("id")));

            data.add(model);
        }
        db.close();

        return data;
    }


    public void update(List<MessageModel> data) {

        SQLiteDatabase db = helper.getWritableDatabase();

        for (MessageModel model : data) {
            ContentValues values = new ContentValues();

            values.put("read", 0);

            int update = db.update(DatabaseConstance.TAB_MESSAGE, values, "id=?", new String[]{model.getId() + ""});
        }

        db.close();
    }

    /**
     * 有未读消息
     * @return
     */
    public boolean findUnread() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "select * from " + DatabaseConstance.TAB_MESSAGE + " where read=-1 order by id desc";
//        Cursor cursor = db.query(DatabaseConstance.TAB_MESSAGE, null, "read=?", new String[]{"-1"}, null, null, "id desc");
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToNext()) {
            return true;
        }
        return false;

    }

    public void add(MessageModel model) {

        if (model != null && !TextUtils.isEmpty(model.getContent()) && !TextUtils.isEmpty(model.getTime())) {
            try {
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("time", model.getTime());
                values.put("content", model.getContent());
                values.put("read", model.getRead() + "");

                db.insert(DatabaseConstance.TAB_MESSAGE, null, values);

                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
