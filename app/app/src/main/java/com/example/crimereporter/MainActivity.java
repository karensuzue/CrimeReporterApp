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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tvLocation, tvDescription, temp;
    Button btnRefresh, btnForm;

    private double longitude = -34.44076, latitude = -58.70521;
    private String result = "";
    private RequestQueue requestQueue;

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
                //initLocation();
                getAddress();
                String s = "You're at " + result + ". Here are the crimes in your area:";
                tvDescription.setText(s);
                tvDescription.setVisibility(View.VISIBLE);
                //String s1 = Double.toString(longitude);
                //String s2 = Double.toString(latitude);
                //tvDescription.setText("Longitude: " + s1 + ", Latitude: " + s2);
                //tvDescription.setVisibility(View.VISIBLE);
            }
        });
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        initLocation();
    }*/

    /*public void showRationale() {
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
    }*/

    /*public String getAddress(Location location, Handler handler) {
        Thread thread = new Thread();
        String result = "";

        final Geocoder geocoder = new Geocoder(this,
                Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude,
                    1);
            Address address = addresses.get(0);
            result = address.getAddressLine(0) + " " + address.getLocality();
            Log.d("mylog", "Complete address", result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }*/

    public void getAddress() {
        String url = "https://nominatim.openstreetmap.org/reverse?lat=" + latitude + "&lon=" +
                longitude + "&format=jsonv2";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray keys = response.names();
                            for (int i = 0; i < keys.length(); i++) {
                                String key = keys.getString(i);
                                if (key == "display_name") {
                                    result = response.getString(key);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }


    public Location initLocation() {
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

        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
                    10, listener);
        }

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
            }
            else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}