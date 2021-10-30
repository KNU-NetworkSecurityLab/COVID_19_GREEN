package com.example.uc_covid_19_green;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Utils {
    LocationManager locationManager;

    public static URL getListURL() {
        URL url = null;
        try {
            url = new URL("http://yoon915.dothome.co.kr/list.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public String onLocate(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "권한 필요";
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final String[] strAddress = {""};

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String proivder = location.getProvider();
                double longitude = location.getLongitude(); //위도
                double latitude = location.getLatitude(); //경도
                double altitude = location.getAltitude(); //고도

                Log.d("Location", longitude + " " + latitude);

                Geocoder g = new Geocoder(context);
                List<Address> address = null;
                try {
                    address = g.getFromLocation(latitude,longitude,10);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                strAddress[0] = address.get(0).getAddressLine(0);


            }
        };


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, locationListener);

        return strAddress[0];
    }

    class GetList extends AsyncTask<String, Void, String> {
        String str = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = Utils.getListURL();
                URLConnection con = url.openConnection();

                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                String line = "";

                str = rd.readLine();
                while ((line = rd.readLine()) != null) {
                    str = line;
                    //주의 : str = str + line 으로 받을 경우 앞의 쓰레기값까지 싹 다 받음
                    //어차피 return 되는 jsonarray는 한줄로 싹다 출력되기에 마지막줄만 받으면 됨
                }
                //서버에서 return 해준 jsonarray값이 str에 저장됨

            } catch (Exception e) {
                e.printStackTrace();
            }
            ;

            return str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}
