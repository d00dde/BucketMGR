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

    public void writeTransaction (int id_cust, int id_goods, int val) {
        ContentValues cv = new ContentValues ();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put ("id_cust", id_cust);
        Cursor c = db.query("Customers", null, null, null, null, null, null);
        if(c.moveToFirst()){
            int id_col = c.getColumnIndex("id");
            do{
                if(c.getInt(id_col) == id_cust) {
                    cv.put ("name_cust", c.getString(c.getColumnIndex("name")));
                    break;
                }
            } while (c.moveToNext());
        }
        c.close();

        cv.put ("id_goods", id_goods);
        c = db.query("Goods", null, null, null, null, null, null);
        if(c.moveToFirst()){
            int id_col = c.getColumnIndex("id");
            do{
                if(c.getInt(id_col) == id_cust) {
                    cv.put ("name_goods", c.getString(c.getColumnIndex("name")));
                    break;
                }
            } while (c.moveToNext());
        }
        c.close();
        cv.put ("value", val);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        cv.put ("date", sdf.format(new Date(System.currentTimeMillis())));
        long tactionID = db.insert("Transactions", null, cv);
        dbHelper.close();
    }

    public void initDB () {
        addCustomer ("Первый");
        addCustomer ("Второй");
        addCustomer ("Третий");
        addCustomer ("Четвертый");
        addCustomer ("Пятый");
        addGoods("Первое");
        addGoods("Второе");
        addGoods("Третье");
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

    public void getDebtsfromCust (int id_cust, Map<Integer, Integer> map) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("Select id_goods, SUM(value) as SumVal from Transactions where id_cust = " + id_cust + " group by id_goods" , null);
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

    public void getDebtsfromGoods (int id_goods, Map<Integer, Integer> map) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("Select id_cust, SUM(value) as SumVal from Transactions where id_goods = " + id_goods + " group by id_goods" , null);
        if(c.moveToFirst()) {
            int id_cust_column = c.getColumnIndex("id_cust");
            int sum_column = c.getColumnIndex("SumVal");
            do {
                if (c.getInt(sum_column) != 0)
                    map.put(c.getInt(id_cust_column),c.getInt(sum_column));
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

    public String getGoodsName (int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("Goods", null, null, null, null, null, null);
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

    public void delGoods (int id_cust) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Goods", "id =" + id_cust, null);
        db.close();
    }


}


