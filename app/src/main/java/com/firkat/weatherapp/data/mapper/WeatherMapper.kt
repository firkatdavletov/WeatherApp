package com.firkat.weatherapp.data.mapper

import com.firkat.weatherapp.data.network.dto.WeatherCurrentDto
import com.firkat.weatherapp.data.network.dto.WeatherDto
import com.firkat.weatherapp.data.network.dto.WeatherForecastDto
import com.firkat.weatherapp.domain.entity.Forecast
import com.firkat.weatherapp.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toEntity(): Weather = current.toEntity()

fun WeatherDto.toEntity(): Weather = Weather(
    tempC = tempC,
    conditionText = condition.text,
    conditionUrl = condition.icon.correctImageUrl(),
    date = date.toCalendar()
)

fun WeatherForecastDto.toEntity(): Forecast = Forecast(
    currentWeather = current.toEntity(),
    upcoming = forecast.forecastDay.drop(1).map {
        val weatherDto = it.day
        Weather(
            tempC = weatherDto.tempC,
            conditionText = weatherDto.condition.text,
            conditionUrl = weatherDto.condition.icon.correctImageUrl(),
            date = it.date.toCalendar()
        )
    }
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

private fun String.correctImageUrl(): String = "https:$this".replace(
    oldValue = "64x64",
    newValue = "128x128"
)