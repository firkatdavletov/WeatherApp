package com.firkat.weatherapp.domain.repository

import com.firkat.weatherapp.domain.entity.City

interface SearchRepository {
    suspend fun search(query: String): List<City>
}