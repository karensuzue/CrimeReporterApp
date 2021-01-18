package com.example.crimereporter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnForm= findViewById(R.id.btnForm);
        Intent toForm= new Intent(MainActivity.this, com.example.crimereporter.Form.class);
        startActivity(toForm);
    }
}