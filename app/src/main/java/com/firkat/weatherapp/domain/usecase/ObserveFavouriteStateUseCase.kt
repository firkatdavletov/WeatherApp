package com.firkat.weatherapp.domain.usecase

import com.firkat.weatherapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class ObserveFavouriteStateUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {
    operator fun invoke(cityId: Int) = favouriteRepository.observeIsFavourite(cityId)
}