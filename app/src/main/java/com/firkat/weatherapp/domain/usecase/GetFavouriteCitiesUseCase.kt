package com.firkat.weatherapp.domain.usecase

import com.firkat.weatherapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {
    operator fun invoke() = favouriteRepository.favouriteCities
}