package com.example.projectplswork;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WeatherActivity extends AppCompatActivity{
    private TextView weatherTextView;

    private EditText editMunicipalityWeather;
    private String municipality;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = findViewById(R.id.weatherTextView);

        Button fetchWeatherButton = findViewById(R.id.fetchWeatherButton);
        editMunicipalityWeather = findViewById(R.id.editMunicipalityWeather);
        fetchWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String municipality = editMunicipalityWeather.getText().toString();
                WeatherAPI.fetchWeatherData(municipality, new WeatherAPI.WeatherCallback() {
                    @Override
                    public void onWeatherDataReceived(String temperature) {
                        String weatherInfo = "Current temperature in " + municipality + ": " + temperature + "°C";
                        weatherTextView.setText(weatherInfo);
                    }

                    @Override
                    public void onError(String message) {
                    }
                });
            }
        });
    }


    public String getMunicipality() {
        return  municipality;
    }
}
