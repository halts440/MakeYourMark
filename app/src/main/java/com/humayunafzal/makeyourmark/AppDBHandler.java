package com.humayunafzal.makeyourmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

public class AppDBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mym.db";

    public AppDBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static class UserInfo implements BaseColumns
    {
        public static final String TABLE_NAME = "userInfo";
        public static final String CN_PHONE = "phone";
        public static final String CN_STATUS = "status";
    }

    private static final String SQL_CREATE_UserInfoTable =
            "CREATE TABLE " + UserInfo.TABLE_NAME + " (" +
                    UserInfo._ID + " INTEGER PRIMARY KEY," +
                    UserInfo.CN_PHONE + " TEXT," +
                    UserInfo.CN_STATUS + " TEXT)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_UserInfoTable);
        // add a default user in database
        ContentValues defaultUser = new ContentValues();
        defaultUser.put(UserInfo.CN_PHONE, "NULL");
        defaultUser.put(UserInfo.CN_STATUS, "0");
        db.insert(UserInfo.TABLE_NAME, null, defaultUser);
        Log.d("abc", "RRRT");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) { }
}



