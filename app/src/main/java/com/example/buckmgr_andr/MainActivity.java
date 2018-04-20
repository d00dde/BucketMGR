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
    public void onCustClick (View v) {
        Intent intent = new Intent(this,CustomerActivity.class);
        startActivity(intent);
    }

    public void onGoodsClick (View v) {
        Intent intent = new Intent(this,GoodsActivity.class);
        startActivity(intent);
    }

}
