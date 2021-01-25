package com.example.crimereporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    TextView tvLocation, tvDescription, temp;
    Button btnRefresh, btnForm;

    double longitude, latitude;
    ArrayList<String> descriptions;


    Button btnForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvLocation = findViewById(R.id.tvLocation);
        tvDescription = findViewById(R.id.tvDescription);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnForm = findViewById(R.id.btnForm);

        tvDescription.setVisibility(View.GONE);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                showAlertDialog();
            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
                    10, listener);
        }
        catch (SecurityException e) {
            showAlertDialog();
        }


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = Double.toString(longitude);
                String s2 = Double.toString(latitude);
                tvDescription.setText("Longitude: " + s1 + ", Latitude: " + s2);
                tvDescription.setVisibility(View.VISIBLE);

            }
        });
    }


    public void showAlertDialog() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Request location");
        builder.setMessage("GPS is disabled. In order to use the application, " +
                "you need to enable your GPS. Tap OK to go to Settings.");
        builder.setCancelable(true);

        //Alert Dialog: OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        //Alert Dialog: EXIT button
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        btnForm= findViewById(R.id.btnForm);
        Intent toForm= new Intent(MainActivity.this, com.example.crimereporter.Form.class);
        startActivity(toForm);

    }
}