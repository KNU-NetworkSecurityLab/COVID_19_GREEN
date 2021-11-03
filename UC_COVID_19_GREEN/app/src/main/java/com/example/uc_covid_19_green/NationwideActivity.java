package com.example.uc_covid_19_green;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NationwideActivity extends AppCompatActivity {
    TextView tv_table;
    String db_name[];
    String strResult = "";
    int db_distance[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationwide);
        tv_table = findViewById(R.id.tv_table);


        GetList gl = new GetList();


        String str = null;
        try {
            str = gl.execute().get();

            JSONObject Jasonobject = new JSONObject(str);
            JSONArray jsonArray = Jasonobject.getJSONArray("response");

            db_name = new String[jsonArray.length()];
            db_distance = new int[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("json123", jsonObject.getInt("CityMeetp") + "");
                db_name[i] = jsonObject.getString("CityName");
                db_distance[i] = jsonObject.getInt("CityDistance");

                strResult = strResult + db_name[i] + " | " + db_distance[i] + "\n";
            }
        } catch (Exception e) {
        }


        tv_table.setText(strResult);
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

}