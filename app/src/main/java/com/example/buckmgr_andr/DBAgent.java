package com.example.buckmgr_andr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        cv.put ("name_cust", c.getString(2));
        c.close();
        c = db.query("Goods", null, null, null, null, null, null);
        c.moveToPosition (id_goods);
        cv.put ("id_goods", id_goods);
        cv.put ("name_goods", c.getString(2));
        c.close();
        cv.put ("value", val);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        cv.put ("date", sdf.format(new Date(System.currentTimeMillis())));
        long tactionID = db.insert("Transactions", null, cv);
        dbHelper.close();
    }

    public void initDB () {
        addCustomer ("Cust_1");
        addCustomer ("Cust_2");
        addCustomer ("Cust_3");
        addGoods("Goods_1");
        addGoods("Goods_2");
        addGoods("Goods_3");
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
            int i = 0;
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

    public void getAllDebts (int id_cust, int[] debts) {

        for(int i = 0; i < currGoods; i++)
            debts[i] = 5 + i;



        /*SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("Select id_goods, SUM(value) as SumVal from Transactions on id_cust = " + id_cust + " sort by id_goods", null);
        if(c.moveToFirst()) {
            int pos = c.getColumnIndex("SumVal");
            do {
                debt[c.getColumnIndex("id_goods")] = c.getInt(pos);
            } while(c.moveToNext());
        }
        c.close();
        dbHelper.close();*/
    }

    public String getCustName (int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("Customers", null, null, null, null, null, null);
        String str = "Неизвестный заказчик";
        if (c.moveToPosition(id-1))
            str = c.getString(c.getColumnIndex("name"));
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


