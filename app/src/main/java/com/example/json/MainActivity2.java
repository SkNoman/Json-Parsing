package com.example.json;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
      
        listView = findViewById(R.id.listview);
        JsonTask jsonTask = new JsonTask();
        jsonTask.execute();

    }

    public class JsonTask extends AsyncTask<String,String,List<DemoStudent> >{


        @Override
        protected List<DemoStudent>doInBackground(String... strings) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String JsonFile;

            try {
                URL url = new URL("https://raw.githubusercontent.com/sknoman77/jsontest/main/db.json");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line="";
                while ((line=bufferedReader.readLine())!=null)
                {
                    stringBuffer.append(line);
                }

                JsonFile = stringBuffer.toString();
                JSONObject mainObject = new JSONObject(JsonFile);
                JSONArray studentArray = mainObject.getJSONArray("studentAddress");
                List<DemoStudent> demoStudentslist = new ArrayList<>();
                for(int i = 0; i<studentArray.length();i++)
                {
                    JSONObject ArrayObjects = studentArray.getJSONObject(i);
                    DemoStudent demoStudent = new DemoStudent();
                    demoStudent.setName(ArrayObjects.getString("Name"));
                    demoStudent.setId(ArrayObjects.getInt("id"));
                    demoStudent.setCountry(ArrayObjects.getString("locality"));
                    demoStudent.setHno(ArrayObjects.getInt("H.No"));
                    demoStudentslist.add(demoStudent);
                }

                return demoStudentslist;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                try {
                    httpURLConnection.disconnect();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<DemoStudent> s) {
            super.onPostExecute(s);

            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),R.layout.sample,s);
            listView.setAdapter(customAdapter);
        }
    }
}