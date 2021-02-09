package com.example.crimereporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextView tvLocation, tvDescription;
    Button btnRefresh, btnForm;

    //private double longitude = -34.44076, latitude = -58.70521;
    public static double longitude = 0, latitude = 0;

    public static int LOCATION_PERMISSION_REQUEST = 100;

    FirebaseDatabase database= FirebaseDatabase.getInstance("https://crime-reporter-8e0ac-default-rtdb.firebaseio.com/");
    DatabaseReference databaseReference= database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        tvLocation = findViewById(R.id.tvLocation);
        tvDescription = findViewById(R.id.tvDescription);
        btnForm = findViewById(R.id.btnForm);

        tvDescription.setVisibility(View.GONE);

        btnForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toForm = new Intent(MainActivity.this, com.example.crimereporter.Form.class);
                startActivity(toForm);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnRefresh = findViewById(R.id.btnRefresh);
        initLocation();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getAddress();
                //String s = "You're at " + result + ". Here are the crimes in your area:";
                //tvDescription.setText(s);
                //tvDescription.setVisibility(View.VISIBLE);
                String s1 = Double.toString(longitude);
                String s2 = Double.toString(latitude);
                tvDescription.setText("Longitude: " + s1 + ", Latitude: " + s2);
                tvDescription.setVisibility(View.VISIBLE);
            }
        });
    }

    public void initLocation() {
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
                checkPermission();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                checkPermission();
            }
        };

        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
                    10, listener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
    }


    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST);
                return false;
            }

            else {
                Toast.makeText(this, "Permission already granted.", Toast.LENGTH_SHORT)
                        .show();

                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            //If request is not cancelled aka result array is not empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}