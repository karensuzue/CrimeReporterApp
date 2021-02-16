package com.example.crimereporter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tvLocation, tvDescription;
    Button btnRefresh, btnForm;
    ListView lvList;
    Form.Location location;

    //private double longitude = -34.44076, latitude = -58.70521;
    public static double longitude = 0.0, latitude = 0.0;
    public static String locName = "LocationName"; //if reverse Geocoder works this would change

    public static int LOCATION_PERMISSION_REQUEST = 100;

    ArrayList<Display> list = new ArrayList<Display>();
    CustomAdapter adapter;
    //ArrayAdapter<Display> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        location = new Form.Location(longitude, latitude, locName);
        tvLocation = findViewById(R.id.tvLocation);
        tvDescription = findViewById(R.id.tvDescription);
        btnForm = findViewById(R.id.btnForm);
        //lvList = findViewById(R.id.lvList);

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
        lvList = findViewById(R.id.lvList);

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
                lvList.setVisibility(View.VISIBLE);
                //update the list fragment
            }
        });

        //adapter = new ArrayAdapter<Display>(this, R.layout.item_display, list);
        adapter = new CustomAdapter(this, list);
        lvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        FirebaseDatabase database= FirebaseDatabase.getInstance("https://crime-reporter-8e0ac-default-rtdb.firebaseio.com/");
        DatabaseReference ref = database.getReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Double long_map = 0.0, lat_map = 0.0;
                    if (map.get("longitude") instanceof Double) {
                        long_map = ((Double) map.get("longitude")).doubleValue();
                    }

                    if (map.get("latitude") instanceof Double) {
                        lat_map = ((Double) map.get("latitude")).doubleValue();
                    }

                    if (long_map == longitude && lat_map == latitude) {
                        String title = (String) map.get("title");
                        String time = (String) map.get("time");
                        String type = (String) map.get("typeOfCrime");
                        String description = (String) map.get("description");
                        Display entry = new Display(title, time, type, description);
                        adapter.add(entry);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Database read failed " + error.getCode());
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
            //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //longitude = location.getLongitude();
            //latitude = location.getLatitude();
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