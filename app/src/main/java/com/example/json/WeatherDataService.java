package com.example.json;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

      public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
      public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";
      Context context;
      String cityId;

      public WeatherDataService(Context context) {
            this.context = context;
      }

      public interface VolleyResponseListener
      {
            void onError(String message);

            void onResponse(String cityId);
      }
      public void getCityId(String cityName, VolleyResponseListener volleyResponseListener)
      {
            //RequestQueue queue = Volley.newRequestQueue(this);
            String url = QUERY_FOR_CITY_ID + cityName;
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONArray>() {
                  @Override
                  public void onResponse(JSONArray response) {
                         cityId="";
                        JSONObject cityInfo  = null;
                        try {
                              cityInfo = response.getJSONObject(0);
                              cityId = cityInfo.getString("woeid");
                        } catch (JSONException e) {
                              e.printStackTrace();
                        }
                        //this worked but it didn't retured the number in employee info
                        //Toast.makeText(context, "City Id: "+cityId, Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onResponse(cityId);

                  }
            }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context, "Something Wrong..", Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onError("Something Wrong..");
                  }
            }
            );
            // queue.add(request);

            MySingleton.getInstance(context).addToRequestQueue(request);// using MySingleton Class
            //return cityId; // returing nothing
      }


      public interface ForeCastByIdResponse
      {
            void onError(String message);

            void onResponse(List<WeatherReportModel> weatherReportModels);
      }
      public void getCityForecastById(String cityId, ForeCastByIdResponse foreCastByIdResponse)
      {
            List<WeatherReportModel> weatherReportModels = new ArrayList<>();
            String url = QUERY_FOR_CITY_WEATHER_BY_ID+ cityId;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {
                       // Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                              JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");

                              for (int i=0;i<consolidated_weather_list.length();i++)
                              {
                                    WeatherReportModel one_day_weather_list = new WeatherReportModel();
                                    JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);
                                    one_day_weather_list.setId(first_day_from_api.getInt("id"));
                                    one_day_weather_list.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                                    one_day_weather_list.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                                    one_day_weather_list.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                                    one_day_weather_list.setCreated(first_day_from_api.getString("created"));
                                    one_day_weather_list.setApplicable_date(first_day_from_api.getString("applicable_date"));
                                    one_day_weather_list.setMin_temp(first_day_from_api.getLong("min_temp"));
                                    one_day_weather_list.setMax_temp(first_day_from_api.getLong("max_temp"));
                                    one_day_weather_list.setThe_temp(first_day_from_api.getLong("the_temp"));
                                    one_day_weather_list.setWind_speed(first_day_from_api.getLong("wind_speed"));
                                    one_day_weather_list.setWind_direction(first_day_from_api.getLong("wind_direction"));
                                    one_day_weather_list.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                                    one_day_weather_list.setHumidity(first_day_from_api.getInt("humidity"));
                                    one_day_weather_list.setVisibility(first_day_from_api.getLong("visibility"));
                                    one_day_weather_list.setPredictability(first_day_from_api.getInt("predictability"));
                                    weatherReportModels.add(one_day_weather_list);
                              }
                              foreCastByIdResponse.onResponse(weatherReportModels);


                        } catch (JSONException e) {
                              e.printStackTrace();
                        }

                  }
            }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {

                  }
            });

            MySingleton.getInstance(context).addToRequestQueue(request);// using MySingleton Class
      }
      //create an interface
     public  interface GetCityForecastByNameCallback
      {
            void onError(String message);
            void onResponse(List<WeatherReportModel> weatherReportModels);
      }
      //interface over


      public void getCityForecastByName(String cityName, GetCityForecastByNameCallback getCityForecastByNameCallback)
      {
             getCityId(cityName, new VolleyResponseListener()
             {
                   @Override
                   public void onError(String message)
                   {
                           //error field
                   }

                   @Override
                   public void onResponse(String cityId)
                   {
                         //response field
                       getCityForecastById(cityId, new ForeCastByIdResponse()
                       {
                             @Override
                             public void onError(String message)
                             {
                                  //in error field
                             }

                             @Override
                             public void onResponse(List<WeatherReportModel> weatherReportModels)
                             {
                                   //in response field
                                   //we have the weather report
                                   getCityForecastByNameCallback.onResponse(weatherReportModels);
                             }
                       });
                   }
             });
      }
}
