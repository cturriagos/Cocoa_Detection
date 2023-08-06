package com.example.cocoa_tester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goAlta (View vista){
        Intent myIntent = new Intent(this, H_Alta.class);
        startActivity(myIntent);
    }

    public void goBaja (View vista){
        Intent myIntent = new Intent(this, H_Baja.class);
        startActivity(myIntent);
    }
}   