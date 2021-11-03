package com.example.uc_covid_19_green;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView tv_location;
    Button btn_refresh;
    TextView tv_main_distance;
    Button btn_scan;
    Button btn_policy;
    Button btn_nationwide;
    LocationManager locationManager;
    String db_name[];
    String db_details[];
    int db_distance[][];
    String strLocation = null;
    int idx = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        btn_refresh = findViewById(R.id.btn_refresh);
        tv_location = findViewById(R.id.tv_location);
        tv_main_distance = findViewById(R.id.tv_main_distance);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        btn_scan = findViewById(R.id.btn_scan);
        btn_policy = findViewById(R.id.btn_policy);
        btn_nationwide = findViewById(R.id.btn_nationwide);



        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRScanActivity.class);
                startActivity(intent);
            }
        });

        btn_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idx == -1) {
                    Toast.makeText(getApplicationContext(), "위치 탐색중입니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), SocialDistanceActivity.class);
                intent.putExtra("root", "Main");
                intent.putExtra("Address", strLocation);
                intent.putExtra("CityName", db_name[idx]);
                intent.putExtra("CityDistance", db_distance[idx][0]);
                intent.putExtra("CityMeet", db_distance[idx][1]);
                intent.putExtra("CityMeetp", db_distance[idx][2]);
                intent.putExtra("CityDetails", db_details[idx]);
                startActivity(intent);
            }
        });

        btn_nationwide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NationwideActivity.class);
                startActivity(intent);
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dev2","devee");
                onLocate();
            }
        });

        onLocate();

        GetList gl = new GetList();

        String tempstr = "";
        try {
            tempstr = gl.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("temp", tempstr);

        try {
//                Log.d("dt","1");
//                JSONArray jsonArray = new JSONArray(str);
//                Log.d("dt","2");

            JSONObject Jasonobject = new JSONObject(tempstr);
            JSONArray jsonArray = Jasonobject.getJSONArray("response");

            db_name = new String[jsonArray.length()];
            db_distance = new int[jsonArray.length()][4];
            db_details = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("json123", jsonObject.getInt("CityMeetp") + "");
                db_name[i] = jsonObject.getString("CityName");
                db_distance[i][0] = jsonObject.getInt("CityDistance");
                db_distance[i][1] = jsonObject.getInt("CityMeet");
                db_distance[i][2] = jsonObject.getInt("CityMeetp");
                db_details[i] = jsonObject.getString("CityDetails");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < db_name.length; i++) {
            Log.d("db_dev1", db_name[i] + " " + db_distance[i][0] + " " + db_distance[i][1] + " " + db_distance[i][2] + " " + db_distance[i][3] + " " + db_details[i]);
        }

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


    public void onLocate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "권한 필요", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("dev1","tv_click");
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String proivder = location.getProvider();
                double longitude = location.getLongitude(); //위도
                double latitude = location.getLatitude(); //경도
                double altitude = location.getAltitude(); //고도

                Geocoder g = new Geocoder(getApplicationContext());
                List<Address> address = null;
                try {
                    address = g.getFromLocation(latitude, longitude, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("찾은 주소", address.get(0).getAddressLine(0));
                tv_location.setText(address.get(0).getAddressLine(0));
                strLocation = address.get(0).getAddressLine(0);
                onLocationFind(address.get(0).getAddressLine(0));
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
    }

    public void onLocationFind(String strLocation) {
        String[] splitedLocation = strLocation.split(" ");

        for (int i = 0; i < splitedLocation.length; i++)
            Log.d("lo", splitedLocation[i]);

        for (int i = 0; i < db_name.length; i++) {
            if (db_name[i].equals(splitedLocation[1])) {
                idx = i;
            }
        }
        Log.d("find", db_name[idx] + " " + db_distance[idx][0] + " " + db_distance[idx][1] + " " + db_distance[idx][2]);
        tv_main_distance.setText(db_distance[idx][0] + "");
    }


    private void getHashKey() {
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

