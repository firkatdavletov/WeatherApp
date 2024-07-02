package com.firkat.weatherapp.domain.repository

import com.firkat.weatherapp.domain.entity.City
import com.firkat.weatherapp.domain.entity.Forecast
import com.firkat.weatherapp.domain.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(cityId: Int): Weather
    suspend fun getForecast(cityId: Int): Forecast
}