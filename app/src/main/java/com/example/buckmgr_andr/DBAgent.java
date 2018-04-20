package com.example.buckmgr_andr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Андрей on 04.04.2018.
 */

public class DBAgent {
    private static DBAgent agent = null;
    private DBHelper dbHelper;
    public static void setDBAgent (Context context) {
            agent = new DBAgent (context);
    }

    public static DBAgent getAgent () {
        return agent;
    }

    private DBAgent (Context context) {
        dbHelper = new DBHelper(context);
        currCustomer = getCustQuen();
        currGoods = getGoodsQuen();
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + "Goods");
        //db.execSQL("DROP TABLE IF EXISTS " + "Transactions");
        //db.execSQL("DROP TABLE IF EXISTS " + "Customers");
        //db.delete ("Customers", null, null);
        //initDB();
    }
    public int currCustomer; // переменная содержит текущее кол-во заказчиков в БД. Служит для сокращения кол-ва запросов к getCustQuen()
    public int currGoods; // переменная содержит текущее кол-во товаров в БД. Служит для сокращения кол-ва запросов к getGoodsQuen()

    public void writeTrans (int id_cust, int id_goods, int val) {
        ContentValues cv = new ContentValues ();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("Customers", null, null, null, null, null, null);
        c.moveToPosition (id_cust);
        cv.put ("id_cust", id_cust);
        cv.put ("name_cust", c.getString(c.getColumnIndex("name")));
        c.close();
        c = db.query("Goods", null, null, null, null, null, null);
        c.moveToPosition (id_goods);
        cv.put ("id_goods", id_goods);
        cv.put ("name_goods", c.getString(c.getColumnIndex("name")));
        c.close();
        cv.put ("value", val);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        cv.put ("date", sdf.format(new Date(System.currentTimeMillis())));
        long tactionID = db.insert("Transactions", null, cv);
        dbHelper.close();
    }

    public void initDB () {
        addCustomer ("Петрович");
        addCustomer ("Степаныч");
        addCustomer ("Джон Коннор");
        addCustomer ("Папа Джонс");
        addGoods("Ведро");
        addGoods("Шмедро");
        addGoods("Ядро");
    }

    public void addCustomer (String name) {
        ContentValues cv = new ContentValues ();
        cv.put ("name", name);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long custID = db.insert("Customers", null, cv);
        dbHelper.close();
        currCustomer = getCustQuen();
    }

    public void addGoods (String name) {
        ContentValues cv = new ContentValues ();
        cv.put ("name", name);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long custID = db.insert("Goods", null, cv);
        dbHelper.close();
        currCustomer = getGoodsQuen();
    }

    public int getCustQuen () {
        int customers = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("Customers", null, null, null, null, null, null);
        if(c.moveToFirst()) {
            do {
                customers++;
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return customers;
    }

    public int getGoodsQuen () {
        int goods = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("Goods", null, null, null, null, null, null);
        if(c.moveToFirst()) {
            do {
                goods++;
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return goods;
    }

    public void getCustNames (Map<Integer, String> map) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("Customers", null, null, null, null, null, null);
        if(c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            int idColIndex = c.getColumnIndex("id");
            do {
                map.put(c.getInt(idColIndex),c.getString(nameColIndex));
            } while(c.moveToNext());
        }
        c.close();
        dbHelper.close();
    }

    public void getGoodsNames (Map<Integer, String> map) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("Goods", null, null, null, null, null, null);
        if(c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            int idColIndex = c.getColumnIndex("id");
            do {
                map.put(c.getInt(idColIndex),c.getString(nameColIndex));
            } while(c.moveToNext());
        }
        c.close();
        dbHelper.close();
    }

    public int getDebt (int id_cust, int id_goods) {
        String sqlQuery = "Select value from Transactions on id_cust = " + id_cust + " while id_goods = " + id_goods;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(sqlQuery, null);
        int debt = 0;
        if(c.moveToFirst()) {
            int pos = c.getColumnIndex("value");
            do {
                debt += c.getInt(pos);
            } while(c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return debt;
    }

    public void getAllDebts (int id_cust, Map<Integer, Integer> map) {

        /*for (int i = 0; i < 5; i++)
            map.put (i, 42 + i); */


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("Select id_goods, SUM(value) as SumVal from Transactions on id_cust = " + id_cust + " sort by id_goods" , null);
        if(c.moveToFirst()) {
            int id_goods_column = c.getColumnIndex("id_goods");
            int sum_column = c.getColumnIndex("SumVal");
            do {
                map.put(c.getInt(id_goods_column),c.getInt(sum_column));
            } while(c.moveToNext());
        }
        c.close();
        dbHelper.close();
    }

    public String getCustName (int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("Customers", null, null, null, null, null, null);
        String str = "Неизвестный заказчик";
        if (c.moveToFirst()) {
            do{
                if(c.getInt(c.getColumnIndex("id")) == id)
                    str = c.getString(c.getColumnIndex("name"));
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return str;
    }

    public void delCustomer (int id_cust) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Customers", "id =" + id_cust, null);
        db.close();
    }

}


