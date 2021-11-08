package com.example.uc_covid_19_green;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class SocialDistanceActivity extends AppCompatActivity {
    String CityName;
    int CityDistance;
    int CityMeet;
    int CityMeetp;
    String CityDetails;
    TextView tv_distance_location;
    TextView tv_distance;
    TextView tv_distance_details;
    TextView tv_details;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_distance);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        tv_distance_location = findViewById(R.id.tv_distance_location);
        tv_distance = findViewById(R.id.tv_distance);
        tv_distance_details = findViewById(R.id.tv_distance_details);
        tv_details = findViewById(R.id.tv_details);

        Intent intent = getIntent();

        String root = intent.getStringExtra("root");

        if (root.equals("QR")) {
            String address = intent.getStringExtra("address");
            QRinit(address);
        } else if (root.equals("Main")) {
            CityName = intent.getStringExtra("CityName");
            CityDistance = intent.getIntExtra("CityDistance", -1);
            CityMeet = intent.getIntExtra("CityMeet", -1);
            CityMeetp = intent.getIntExtra("CityMeetp", -1);
            CityDetails = intent.getStringExtra("CityDetails");

            tv_distance_location.setText(intent.getStringExtra("Address"));
            tv_distance.setText(CityDistance + "");

            String strDetails = CityMeet + "인까지\n";
            strDetails = strDetails + "백신 접종자 포함 시 " + (CityMeetp) + "인까지";
            tv_distance_details.setText(strDetails);
            tv_details.setText(CityDetails);

            onLocate();
        }
    }

    public void QRinit(String address) {
        address = address.replace("\"", "");
        String[] splitedAddress = address.split(" ");

        Utils utils = new Utils();
        String information[] = utils.matchInform(splitedAddress[0]);
        if (utils.matchInform(splitedAddress[0]) == null) {
            information = utils.matchInform(splitedAddress[1]);
        }
        Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
        Log.d("address", address);

        tv_distance_location.setText(address);
        tv_distance.setText(information[1]);
        tv_distance_details.setText(information[2] + "인까지\n백신 접종자 포함 시 " + (information[3]) + "인까지");
        tv_details.setText(information[4]);
    }


    public void onLocate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "권한 필요", Toast.LENGTH_LONG).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String proivder = location.getProvider();
                double longitude = location.getLongitude(); //위도
                double latitude = location.getLatitude(); //경도
                double altitude = location.getAltitude(); //고도

                Log.d("Location", longitude + " " + latitude);

                Geocoder g = new Geocoder(getApplicationContext());
                List<Address> address = null;
                try {
                    address = g.getFromLocation(latitude, longitude, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("찾은 주소", address.get(0).getAddressLine(0));
                tv_distance_location.setText(address.get(0).getAddressLine(0));
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, locationListener);
    }


}