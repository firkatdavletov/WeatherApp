package com.firkat.weatherapp.data.repository

import com.firkat.weatherapp.data.mapper.toEntities
import com.firkat.weatherapp.data.network.api.ApiService
import com.firkat.weatherapp.domain.entity.City
import com.firkat.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): SearchRepository {
    override suspend fun search(query: String): List<City> = apiService.searchCity(query)
        .toEntities()
}