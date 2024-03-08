package com.firkat.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.firkat.weatherapp.data.network.api.ApiFactory
import com.firkat.weatherapp.presentation.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiFactory.apiService

        CoroutineScope(Dispatchers.Main).launch {
            val currentWeather = apiService.loadCurrentWeather("London")
            val forecastWeather = apiService.loadForecast("London")
            val search = apiService.searchCity("London")

            Log.d("MainActivity", "Current weather: $currentWeather\n Forecast: $forecastWeather\n Search: $search")
        }

        setContent {
            WeatherAppTheme {

            }
        }
    }
}