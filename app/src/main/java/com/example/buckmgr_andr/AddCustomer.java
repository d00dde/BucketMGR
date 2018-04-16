package com.example.buckmgr_andr;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCustomer extends AppCompatActivity implements View.OnClickListener{

    EditText name;
    Button btnOK;
    DBAgent dbagent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        dbagent = DBAgent.getAgent();
        name = (EditText) findViewById(R.id.ET_name);
        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
    if (TextUtils.isEmpty(name.getText().toString()))
        Toast.makeText(this, "Имя не указано", Toast.LENGTH_SHORT).show();
    else
        dbagent.addCustomer(name.getText().toString());
        finish();
    }
}
