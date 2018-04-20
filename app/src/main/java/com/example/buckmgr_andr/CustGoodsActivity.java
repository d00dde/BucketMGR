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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_goods);
        dbAgent = DBAgent.getAgent();
        TextView title = (TextView) findViewById(R.id.CustomerName);
        //Button btnDelCast = (Button) findViewById(R.id.btnDelCast);
        //btnDelCast.setOnClickListener(this);

        Intent intent = getIntent();
        cust_id = intent.getIntExtra("coosenCust", -1);
        if (cust_id == -1)
            title.setText("Нет такого клиента");
        else
            Log.d(Global.DTAG, " Запрошено имя для id : " + cust_id);
            title.setText(dbAgent.getCustName(cust_id));


        DBAgent dbagent = DBAgent.getAgent();
        Map<Integer, String> names = new TreeMap<>();
        Map<Integer, Integer> debts = new TreeMap<>();
        dbagent.getGoodsNames(names);
        dbagent.getAllDebts(cust_id,debts);

        LinearLayout scrolLay = (LinearLayout) findViewById(R.id.goodsLay);
        LayoutInflater inflanter = getLayoutInflater();

        for (Map.Entry<Integer, String> customer : names.entrySet()) {
            View item = inflanter.inflate(R.layout.goods_list,scrolLay,false );
            Button btn = (Button) item.findViewById(R.id.btn_goods);
            TextView debt = (TextView) item.findViewById(R.id.debt);
            btn.setText(customer.getValue());
            btn.setId(Global.ID_BIAS + customer.getKey());
            btn.setOnClickListener(this);
            debt.setText("Долг: " + debts.get(customer.getKey()) == null ? 0 : debts.get(customer.getKey()));
            scrolLay.addView(item);

        }
    }


    @Override
    public void onClick(View v) {
        String str = "Нажата кнопка номер " + (v.getId()-Global.ID_BIAS);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void delCustomer (View V) {
        showDialog(DIALOG_DEL);
    }

    protected Dialog onCreateDialog (int id) {
        if (id == DIALOG_DEL){
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Удаление заказчика");
            adb.setMessage("Вы действительно хотите удалить заказчика " + dbAgent.getCustName(cust_id) + "?");
            adb.setPositiveButton("Да", myClickListener);
            adb.setNegativeButton("Нет",myClickListener);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick (DialogInterface dialog, int choose){
            switch (choose){
                case Dialog.BUTTON_POSITIVE:
                    dbAgent.delCustomer(cust_id);
                    Global.isListEdit = true;
                    finish();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }

        }
    };


}
