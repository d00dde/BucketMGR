package com.example.buckmgr_andr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Андрей on 04.04.2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {

        super(context, "myDBase", null, 1);
    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table Customers ("
                + "id integer primary key autoincrement,"
                + "name text" + ");");

        db.execSQL("create table Goods ("
                + "id integer primary key autoincrement,"
                + "name text" + ");");

        db.execSQL("create table Transactions ("
                + "id integer primary key autoincrement,"
                + "id_cust integer,"
                + "name_cust text,"
                + "id_goods integer,"
                + "name_goods text,"
                + "value integer,"
                + "date date" + ");");
        //DBAgent agent = DBAgent.getAgent();
        //agent.initDB();

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
