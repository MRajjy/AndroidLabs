package com.example.melissarajala.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Melissa Rajala on 2017-10-12.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static String ACTIVITY_NAME = "ChatDatabaseHelper";
    public static String DATABASE_NAME = "Messages.db";
    public static int VERSION_NUM = 5;
    public final static String KEY_ID = "_id";
    public final static String KEY_MESSAGE = "MESSAGE";
    public final static String TABLE_NAME = "Lab5";

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase db){
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        //creating a table in the database with 2 columns: Key (integer) and Message (string)
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " text);");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion = " + oldVer + "newVersion = " + newVer);
        //drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //re-create table
        onCreate(db);
    }
}
