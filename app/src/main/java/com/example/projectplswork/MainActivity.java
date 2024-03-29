package com.example.projectplswork;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView weatherTextView;
    private Button fetchWeatherButton;
    private EditText editMunicipalityWeather;
    private String municipality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = findViewById(R.id.weatherTextView);
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton);
        editMunicipalityWeather = findViewById(R.id.editMunicipalityWeather);

        fetchWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                municipality = editMunicipalityWeather.getText().toString();
                new FetchWeatherTask().execute(municipality);
            }
        });
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... cities) {
            try {
                String city = cities[0];
                String apiKey = "f5b239cd950b7ec48958d7cd64f9ef66";
                String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + ",fi&appid=" + apiKey + "&units=metric";

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
                Log.e("FetchWeatherTask", "Error: " + e.getMessage());
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
                    String weatherInfo = "Current temperature in " + municipality + ": " + temperature + "°C";
                    weatherTextView.setText(weatherInfo);
                } catch (JSONException e) {
                    Log.e("FetchWeatherTask", "Error parsing JSON");
                }
            } else {
                Log.e("FetchWeatherTask", "Error fetching weather data");
            }
        }
    }
}