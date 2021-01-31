package com.example.crimereporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tvLocation, tvDescription, temp;
    Button btnRefresh, btnForm;

    private double longitude = 0, latitude = 0;
    //private GpsStatus status;

    public static int LOCATION_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLocation = findViewById(R.id.tvLocation);
        tvDescription = findViewById(R.id.tvDescription);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnForm = findViewById(R.id.btnForm);

        tvDescription.setVisibility(View.GONE);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call checkpermission if its true proceed
                //then call initlocation thing
                checkPermission();

                String s1 = Double.toString(longitude);
                String s2 = Double.toString(latitude);
                tvDescription.setText("Longitude: " + s1 + ", Latitude: " + s2);
                tvDescription.setVisibility(View.VISIBLE);

                /*String locality;

                final Geocoder geocoder = new Geocoder(v.getContext());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude,
                            1);
                    for (Address address : addresses) {
                        if (address.getLocality() != null) {
                            locality = address.getLocality();
                            tvLocation.setText(locality);
                        }
                    }

                    tvDescription.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }
        });
    }


    public void showRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Request location");
        builder.setMessage("GPS is disabled. In order to use the application, " +
                "you need to enable your GPS.");
        builder.setCancelable(true);

        //Alert Dialog: OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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
                //showAlertDialog();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //status = locationManager.getGpsStatus(status);
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
                10, listener);

    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationale();
            }

            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST);
            }
        }

        else {
            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_SHORT)
                    .show();
            initLocation();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            //If request is not cancelled aka result array is not empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                initLocation();
            }
            else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}