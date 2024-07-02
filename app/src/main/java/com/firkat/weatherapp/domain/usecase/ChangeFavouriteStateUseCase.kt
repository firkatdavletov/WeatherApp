package com.firkat.weatherapp.domain.usecase

import com.firkat.weatherapp.domain.entity.City
import com.firkat.weatherapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class ChangeFavouriteStateUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {
    suspend fun addToFavourite(city: City) = favouriteRepository.addToFavourite(city)
    suspend fun removeFromFavourite(cityId: Int) = favouriteRepository.removeFromFavourite(cityId)
}