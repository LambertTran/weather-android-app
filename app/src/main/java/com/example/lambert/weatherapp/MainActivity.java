package com.example.lambert.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView data;
    private String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btnDavis = (Button) findViewById(R.id.davis);
        Button btnSac   = (Button) findViewById(R.id.SacTo);


        data = (TextView) findViewById(R.id.textView);

        btnDavis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link = "http://api.openweathermap.org/data/2.5/weather?q={davis}&appid=973a308c0ade9e3ae157a24a7f619e33&units=imperial";
            }
        });

        btnSac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link = "http://api.openweathermap.org/data/2.5/weather?q={sacramento}&appid=973a308c0ade9e3ae157a24a7f619e33&units=imperial";
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new JSONTask().execute(link);
            }
        });




    }

    public class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {

                    buffer.append(line);
                }


                    String jsonString = buffer.toString();

                    try {

                        JSONObject json = new JSONObject(jsonString);

                        JSONObject main =    json.getJSONObject("main");

                        return "Temperature is " + main.getString("temp");


                    } catch (Exception e) {

                        e.printStackTrace();
                    }


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            data.setText(res.toString());

        }
    }
}
