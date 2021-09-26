package com.example.uc_covid_19_green;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import net.daum.mf.map.api.MapView;

public class ScreeningCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_center);

        MapView mapView = new MapView(this);

        ConstraintLayout mapviewContainer = findViewById(R.id.map_view);
        mapviewContainer.addView(mapView);
    }
}