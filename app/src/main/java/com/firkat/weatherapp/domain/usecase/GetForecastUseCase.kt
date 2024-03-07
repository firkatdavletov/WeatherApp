package com.firkat.weatherapp.domain.usecase

import com.firkat.weatherapp.domain.entity.City
import com.firkat.weatherapp.domain.repository.FavouriteRepository
import com.firkat.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int) = weatherRepository.getForecast(cityId)
}