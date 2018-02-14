package com.jordysantamaria.jordysantamaria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Forzar y cargar icono en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_myicon);

        textView = (TextView) findViewById(R.id.textViewMain);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getString("otherView") != null) {
            String hello = bundle.getString("otherView");
            Toast.makeText(SecondActivity.this, hello, Toast.LENGTH_LONG).show();
            textView.setText(hello);
        } else {
            Toast.makeText(SecondActivity.this, "It is empty", Toast.LENGTH_LONG).show();
        }

    }
}
