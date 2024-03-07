package com.firkat.weatherapp.domain.usecase

import com.firkat.weatherapp.domain.entity.City
import com.firkat.weatherapp.domain.repository.FavouriteRepository
import com.firkat.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String) = searchRepository.search(query)
}