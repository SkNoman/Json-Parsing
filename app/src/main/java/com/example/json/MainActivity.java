package com.example.json;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView t1;
    private Button b1,Emp_info,showset;
    EditText editText;
    String set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = findViewById(R.id.b1);
        t1 = findViewById(R.id.t1);
        Emp_info = findViewById(R.id.emp_info_btn);
        jsonTask jTask = new jsonTask();
        jTask.execute();

        Emp_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(),Employee_Information.class);
                 startActivity(intent);
            }
        });
    }



    public class jsonTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String name;
            Integer id,hno;
            String country;

            try {
                URL url = new URL("https://apex.oracle.com/pls/apex/noman_live_schema/emp_info/");
                //URL url = new URL("https://raw.githubusercontent.com/sknoman77/jsontest/main/db.json");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                StringBuffer lastbuffer = new StringBuffer();

                while((line = bufferedReader.readLine())!=null){
                    stringBuffer.append(line);
                }
                String file = stringBuffer.toString();
                JSONObject fileObject = new JSONObject(file);
                JSONArray jsonArray = fileObject.getJSONArray("items");
                for(int i=0; i<jsonArray.length();i++)
                {
                    JSONObject arrayObject = jsonArray.getJSONObject(i);

                    name = arrayObject.getString("emp_name");
                    country = arrayObject.getString("email_id");
                    id = arrayObject.getInt("mobile_no");
                    hno = arrayObject.getInt("emp_id");


                    lastbuffer.append("Name:"+name+"\n"+"Phone:"+id+"\n"+"Country:"+country+"\n"+"Id:"+hno+"\n\n");

                    //arrayObject.put(String.valueOf(Integer.parseInt("institute_id")),1003);
                    //arrayObject.put("emp_name","Mr Alex");
                     //arrayObject.put("emp_name","Mr Alex");



                }

                return lastbuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t1.setText(s);
                }
            });

        }
    }
}