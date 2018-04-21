package com.example.buckmgr_andr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.deleteDatabase ("myDBase");
        DBAgent.setDBAgent(this);

    }
    public void onClick (View v) {
        switch (v.getId()) {
            case (R.id.btnCust):
                Intent intent_1 = new Intent(this,CustomerActivity.class);
                intent_1.putExtra("type", "customer");
                startActivity(intent_1);
                break;
            case (R.id.btnGoods):
                Intent intent_2 = new Intent(this,CustomerActivity.class);
                intent_2.putExtra("type", "goods");
                startActivity(intent_2);
                break;
            case (R.id.btnAddCust):
                Intent intent_3 = new Intent(this,AddCustomer.class);
                intent_3.putExtra("type", "customer");
                startActivity(intent_3);
                break;
            case (R.id.btnAddGoods):
                Intent intent_4 = new Intent(this,AddCustomer.class);
                intent_4.putExtra("type", "goods");
                startActivity(intent_4);
                break;
        }

    }


}
