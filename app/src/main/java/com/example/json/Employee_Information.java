package com.example.json;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class Employee_Information extends AppCompatActivity
{
    EditText searchEditText;
    Button WeatherIdBtn,WeatherByIdBtn,WeatherByNameBtn;
    ListView listView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_information);
        setTitle("Weather Forecast");
        final  WeatherDataService weatherDataService = new WeatherDataService(Employee_Information.this); // its an instance
        searchEditText = findViewById(R.id.edit_text_search_emp);
        WeatherIdBtn = findViewById(R.id.city_id_btn);
        WeatherByIdBtn = findViewById(R.id.weather_by_id_btn);
        WeatherByNameBtn = findViewById(R.id.weather_by_name_btn);
        progressBar = findViewById(R.id.progressbar_id);
        progressBar.setVisibility(View.GONE);
        listView= findViewById(R.id.listview);
        Toast.makeText(this, "Enter City Name And Weather Details", Toast.LENGTH_SHORT).show();
        WeatherIdBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                weatherDataService.getCityId(searchEditText.getText().toString(), new WeatherDataService.VolleyResponseListener()   //calback
                {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(Employee_Information.this, "Something Wrong ", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String cityId) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Employee_Information.this, "Returned an Id of "+cityId, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        WeatherByIdBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                weatherDataService.getCityForecastById(searchEditText.getText().toString(), new WeatherDataService.ForeCastByIdResponse()
                {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(Employee_Information.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {

                        ArrayAdapter arrayAdapter = new ArrayAdapter(Employee_Information.this, android.R.layout.simple_list_item_1,weatherReportModels);
                        progressBar.setVisibility(View.GONE);
                        listView.setAdapter(arrayAdapter);
                        //Toast.makeText(Employee_Information.this, weatherReportModel.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        WeatherByNameBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                weatherDataService.getCityForecastByName(searchEditText.getText().toString(), new WeatherDataService.GetCityForecastByNameCallback()
                {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(Employee_Information.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {

                        ArrayAdapter arrayAdapter = new ArrayAdapter(Employee_Information.this, android.R.layout.simple_list_item_1,weatherReportModels);
                        progressBar.setVisibility(View.GONE);
                        listView.setAdapter(arrayAdapter);
                        //Toast.makeText(Employee_Information.this, weatherReportModel.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}