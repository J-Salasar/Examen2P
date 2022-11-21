package com.example.examen2p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ActivityInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("entrada","0");
        intent.putExtra("entrada1","0");
        startActivity(intent);
    }
}