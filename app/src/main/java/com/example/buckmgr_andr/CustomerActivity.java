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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        DBAgent dbagent = DBAgent.getAgent();
        names = new TreeMap <>();
        dbagent.getCustNames(names);
        Button addCust = (Button) findViewById(R.id.addCustomer);
        addCust.setText("Добавить заказчика");
        addCust.setOnClickListener(this);
        inflanter = getLayoutInflater();
        scrolLay = (LinearLayout) findViewById(R.id.ScrolLay_1);

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
        String str = "Нажата кнопка номер " + (v.getId()-Global.ID_BIAS);
        if(v.getId() == R.id.addCustomer) {
            Toast.makeText(this, "Нажата добавить заказчика", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AddCustomer.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CustGoodsActivity.class);
            intent.putExtra("choosenCust",v.getId()-Global.ID_BIAS);
            startActivity(intent);
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
