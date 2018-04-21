package com.example.buckmgr_andr;

import android.content.Intent;
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
import java.util.Set;
import java.util.TreeMap;

public class CustomerActivity extends AppCompatActivity implements View.OnClickListener {


    Map <Integer, String> names;
    LayoutInflater inflanter;
    LinearLayout scrolLay;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        DBAgent dbagent = DBAgent.getAgent();
        Intent intent = getIntent();
        type = intent.getStringExtra("type");


        names = new TreeMap <>();

        Button addCust = (Button) findViewById(R.id.addCustomer);
        addCust.setOnClickListener(this);
        inflanter = getLayoutInflater();
        scrolLay = (LinearLayout) findViewById(R.id.ScrolLay_1);

        if ((type.equals("customer"))) {
            dbagent.getCustNames(names);
            addCust.setText("Добавить заказчика");
        }
        else if ((type.equals("goods"))){
            dbagent.getGoodsNames(names);
            addCust.setText("Добавить товар");
        }

        for (Map.Entry<Integer, String> customer : names.entrySet()) {
            View item = inflanter.inflate(R.layout.button_mp,scrolLay,false );
            Button btn = (Button) item.findViewById(R.id.btn_cust);
            btn.setText(customer.getValue());
            btn.setId(Global.ID_BIAS + customer.getKey());
            btn.setOnClickListener(this);
            scrolLay.addView(item);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addCustomer) {
            if ((type.equals("customer"))) {
                Intent intent = new Intent(this, AddCustomer.class);
                intent.putExtra("type", "customer");
                startActivity(intent);
            }
            else if ((type.equals("goods"))){
                Intent intent = new Intent(this, AddCustomer.class);
                intent.putExtra("type", "goods");
                startActivity(intent);
            }
        }
        else {
            if ((type.equals("customer"))) {
                Intent intent = new Intent(this, CustGoodsActivity.class);
                intent.putExtra("choosen",v.getId()-Global.ID_BIAS);
                intent.putExtra("type", "customer");
                startActivity(intent);
            }
            else if ((type.equals("goods"))) {
                Intent intent = new Intent(this, CustGoodsActivity.class);
                intent.putExtra("choosen",v.getId()-Global.ID_BIAS);
                intent.putExtra("type", "goods");
                startActivity(intent);
            }
        }

    }
    public void onRestart () {
        super.onRestart();
        if  (Global.isListEdit) {
            Global.isListEdit = false;
            finish();
        }

    }

}
