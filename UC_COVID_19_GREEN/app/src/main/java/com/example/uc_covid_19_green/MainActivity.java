package com.example.uc_covid_19_green;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.AlgorithmParameterGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv_location;
    Button btn_screening;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_location = findViewById(R.id.tv_location);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        btn_screening = findViewById(R.id.btn_screening);

        btn_screening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScreeningCenterActivity.class);
                startActivity(intent);
            }
        });


        onLocate();
        getHashKey();
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
                    address = g.getFromLocation(latitude,longitude,10);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("찾은 주소",address.get(0).getAddressLine(0));
                tv_location.setText(address.get(0).getAddressLine(0));
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, locationListener);


    }


    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

}
