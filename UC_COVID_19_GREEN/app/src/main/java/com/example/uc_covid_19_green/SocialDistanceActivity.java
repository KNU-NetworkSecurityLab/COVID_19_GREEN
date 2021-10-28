package com.example.uc_covid_19_green;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SocialDistanceActivity extends AppCompatActivity {
    String CityName;
    int CityDistance;
    int CityMeet;
    int CityMeetp;
    TextView tv_distance_location;
    TextView tv_distance;
    TextView tv_distance_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_distance);

        tv_distance_location = findViewById(R.id.tv_distance_location);
        tv_distance = findViewById(R.id.tv_distance);
        tv_distance_details = findViewById(R.id.tv_distance_details);

        Intent intent = getIntent();

        CityName = intent.getStringExtra("CityName");
        CityDistance = intent.getIntExtra("CityDistance", -1);
        CityMeet = intent.getIntExtra("CityMeet", -1);
        CityMeetp = intent.getIntExtra("CityMeetp", -1);

        tv_distance_location.setText(CityName);
        tv_distance.setText(CityDistance+"");

        String strDetails = CityMeet+"인까지\n";
        strDetails = strDetails + "백신 접종자 포함 시 " + (CityMeet+CityMeetp) +"인까지";
        tv_distance_details.setText(strDetails);
    }
}