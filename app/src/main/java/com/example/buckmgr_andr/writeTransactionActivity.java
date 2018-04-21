package com.example.buckmgr_andr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class writeTransactionActivity extends AppCompatActivity implements View.OnClickListener {

    int cust_id;
    int goods_id;
    EditText value;
    Context cont = this;
    DBAgent dbAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_transaction);

        TextView custName = (TextView) findViewById(R.id.viewTxtCustName);
        TextView goodsName = (TextView) findViewById(R.id.viewTxtGoodsName);
        value = (EditText) findViewById(R.id.editTxtValue);
        value.setInputType(InputType.TYPE_CLASS_PHONE);
        Button write = (Button) findViewById(R.id.btnWrite);
        write.setOnClickListener(this);

        dbAgent = DBAgent.getAgent();

        /*value.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //if (keyCode == KeyEvent.KEYCODE_ENTER)
                    Toast.makeText(cont, "Нажата кнопка" + keyCode, Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/

        Intent intent = getIntent();
        cust_id = intent.getIntExtra("choosenCust", -1);
        if (cust_id == -1)
            custName.setText("Нет такого клиента");
        else
            custName.setText(dbAgent.getCustName(cust_id));
        goods_id = intent.getIntExtra("choosenGoods", -1);
        if (cust_id == -1)
            goodsName.setText("Нет такого товара");
        else
            goodsName.setText(dbAgent.getGoodsName(goods_id));

    }


    @Override
    public void onClick(View v) {
        int val = Integer.parseInt(value.getText().toString());
        dbAgent.writeTransaction(cust_id,goods_id, val);
         Global.isListEdit = true;
        Toast.makeText(cont, "Записана транзакция " + val + " штук.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
