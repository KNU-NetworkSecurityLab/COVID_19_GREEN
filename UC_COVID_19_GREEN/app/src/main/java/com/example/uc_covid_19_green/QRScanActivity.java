package com.example.uc_covid_19_green;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "스캔 취소됨", Toast.LENGTH_LONG).show();
                finish();
            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SocialDistanceActivity.class);
                intent.putExtra("address", result.getContents());
                intent.putExtra("root","QR");
                startActivity(intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}