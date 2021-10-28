package com.example.uc_covid_19_green;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NationwideActivity extends AppCompatActivity {
    WebView wv_nation;
    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationwide);

       // wv_nation = findViewById(R.id.wv_nation);

//        wv_nation.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
//        mWebSettings = wv_nation.getSettings(); //세부 세팅 등록
//        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
//        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
//        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
//        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
//        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
//        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
//        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
//        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
//        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
//
//        wv_nation.loadUrl("http://ncov.mohw.go.kr/"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
    }
}