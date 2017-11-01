package com.galaxy.safe.utils;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Dell on 2016/12/7.
 */
public class UserUtils {

    private String person_name;
    private String user_porperty;

    public UserUtils(SQLiteDatabase sdb, Context context) {
        String user = SpUtils.getString(context, "user", "");
        if (user != null) {
            Cursor cursor1 = sdb.query("galaxy_detection_person", new String[]{"user_name", "person_name", "person_property"}, "user_name=?", new String[]{user}, null, null, null);
            while (cursor1.moveToNext()) {
                person_name = cursor1.getString(1);
                user_porperty = cursor1.getString(2);
            }
            cursor1.close();
        }

    }

    public String getPorperty() {

        return user_porperty.equals("") ? "1" : user_porperty;
    }

    public String getPerson_name() {

        return person_name.equals("") ? "1" : person_name;
    }
}
