package com.example.buckmgr_andr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.TreeMap;

public class CustGoodsActivity extends AppCompatActivity implements View.OnClickListener{

    final int DIALOG_DEL = 1;
    DBAgent dbAgent;
    int cust_id = -1;
    String type;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_goods);
        dbAgent = DBAgent.getAgent();
        TextView title = (TextView) findViewById(R.id.CustomerName);
        Button btndel = (Button) findViewById(R.id.btnDelCast);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        cust_id = intent.getIntExtra("choosen", -1);
        if (cust_id == -1)
            title.setText("Нет такой записи");

        Map<Integer, String> names = new TreeMap<>();
        Map<Integer, Integer> debts = new TreeMap<>();

        LinearLayout scrolLay = (LinearLayout) findViewById(R.id.goodsLay);
        LayoutInflater inflanter = getLayoutInflater();

        if ((type.equals("customer"))) {
            title.setText(dbAgent.getCustName(cust_id));
            btndel.setText("Удалить заказчика");
            dbAgent.getGoodsNames(names);
            dbAgent.getDebtsfromCust(cust_id,debts);
            for (Map.Entry<Integer, String> current : names.entrySet()) {
                View item = inflanter.inflate(R.layout.goods_list,scrolLay,false );
                Button btn = (Button) item.findViewById(R.id.btn_goods);
                TextView debt = (TextView) item.findViewById(R.id.debt);
                btn.setText(current.getValue());
                btn.setId(Global.ID_BIAS + current.getKey());
                btn.setOnClickListener(this);
                String temp = debts.get(current.getKey()) == null ? "0" : debts.get(current.getKey()).toString();
                debt.setText("Долг: " + temp );
                scrolLay.addView(item);
            }
        }
        else if ((type.equals("goods"))) {
            title.setText(dbAgent.getGoodsName(cust_id));
            btndel.setText("Удалить товар");
            dbAgent.getCustNames(names);
            dbAgent.getDebtsfromGoods(cust_id,debts);
            for (Map.Entry<Integer, String> current : names.entrySet()) {
                if (debts.get(current.getKey()) != null) {
                    View item = inflanter.inflate(R.layout.goods_list, scrolLay, false);
                    Button btn = (Button) item.findViewById(R.id.btn_goods);
                    TextView debt = (TextView) item.findViewById(R.id.debt);
                    btn.setText(current.getValue());
                    btn.setId(Global.ID_BIAS + current.getKey());
                    btn.setOnClickListener(this);
                    debt.setText("Долг: " + debts.get(current.getKey()).toString());
                    scrolLay.addView(item);
                }
            }
        }

    }


    @Override
    public void onClick(View v) {
        String str = "Нажата кнопка номер " + (v.getId()-Global.ID_BIAS);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, writeTransactionActivity.class);
        if ((type.equals("customer"))){
            intent.putExtra("choosenCust",cust_id);
            intent.putExtra("choosenGoods",v.getId()-Global.ID_BIAS);
        }
        else if ((type.equals("goods"))) {
            intent.putExtra("choosenCust",v.getId()-Global.ID_BIAS);
            intent.putExtra("choosenGoods",cust_id);
        }
        startActivity(intent);

    }

    public void delCustomer (View V) {
        showDialog(DIALOG_DEL);
    }

    protected Dialog onCreateDialog (int id) {
        if (id == DIALOG_DEL){
            if ((type.equals("customer"))) {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("Удаление заказчика");
                adb.setMessage("Вы действительно хотите удалить заказчика " + dbAgent.getCustName(cust_id) + "?");
                adb.setPositiveButton("Да", myClickListener);
                adb.setNegativeButton("Нет",myClickListener);
                return adb.create();
            }
            else if ((type.equals("goods"))) {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("Удаление товара");
                adb.setMessage("Вы действительно хотите удалить товар " + dbAgent.getGoodsName(cust_id) + "?");
                adb.setPositiveButton("Да", myClickListener);
                adb.setNegativeButton("Нет",myClickListener);
                return adb.create();
            }
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick (DialogInterface dialog, int choose){
            switch (choose){
                case Dialog.BUTTON_POSITIVE:
                    if ((type.equals("customer"))) {
                        dbAgent.delCustomer(cust_id);
                    }
                    else if ((type.equals("goods"))) {
                        dbAgent.delGoods(cust_id);
                    }
                    dbAgent.delCustomer(cust_id);
                    Global.isListEdit = true;
                    finish();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }

        }
    };

    public void onRestart() {
        super.onRestart();
        if (Global.isListEdit) {
            Global.isListEdit = false;
            finish();
        }
    }

}
