package com.example.projectplswork;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {



    public interface WeatherCallback {
        void onWeatherDataReceived(String temperature);
        void onError(String message);
    }

    public static void fetchWeatherData(String municipality, WeatherCallback callback) {
        new FetchWeatherTask(municipality, callback).execute();
    }

    private static class FetchWeatherTask extends AsyncTask<Void, Void, String> {
        private WeatherCallback callback;
        private String municipality;

        public FetchWeatherTask(String municipality, WeatherCallback callback) {
            this.municipality = municipality;
            this.callback = callback;
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                WeatherActivity weatherActivity = new WeatherActivity();
                String municipality = weatherActivity.getMunicipality();
                String apiKey = "f5b239cd950b7ec48958d7cd64f9ef66";

                String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+municipality+",fi&appid=" + apiKey + "&units=metric";

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);
            if (jsonResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONObject mainObject = jsonObject.getJSONObject("main");
                    double temperature = mainObject.getDouble("temp");
                    callback.onWeatherDataReceived(String.valueOf(temperature));
                } catch (JSONException e) {
                    callback.onError("Error parsing JSON");
                }
            } else {
                callback.onError("Error fetching weather data");
            }
        }
    }

}
