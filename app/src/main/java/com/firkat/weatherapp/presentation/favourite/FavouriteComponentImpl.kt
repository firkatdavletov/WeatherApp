package com.firkat.weatherapp.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.firkat.weatherapp.domain.entity.City
import com.firkat.weatherapp.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteComponentImpl @Inject constructor(
    private val factory: FavouriteStoreFactory,
    private val onCityItemClicked: (City) -> Unit,
    private val onAddFavouriteClicked: () -> Unit,
    private val onSearchClicked: () -> Unit,
    componentContext: ComponentContext
) : FavouriteComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { factory.create() }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavouriteStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is FavouriteStore.Label.CityItemClicked -> {
                        onCityItemClicked(label.city)
                    }
                    FavouriteStore.Label.ClickSearch -> {
                        onSearchClicked()
                    }
                    FavouriteStore.Label.ClickToFavourite -> {
                        onAddFavouriteClicked()
                    }
                }
            }
        }
    }

    override fun onClickSearch() {
        store.accept(FavouriteStore.Intent.ClickSearch)
    }

    override fun onClickAddFavourite() {
        store.accept(FavouriteStore.Intent.ClickAddToFavourite)
    }

    override fun onCityItemClick(city: City) {
        store.accept(FavouriteStore.Intent.CityItemClicked(city))
    }

}