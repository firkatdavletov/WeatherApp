package com.firkat.weatherapp.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.firkat.weatherapp.domain.entity.City
import com.firkat.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.firkat.weatherapp.domain.usecase.GetFavouriteCitiesUseCase
import com.firkat.weatherapp.presentation.favourite.FavouriteStore.Intent
import com.firkat.weatherapp.presentation.favourite.FavouriteStore.Label
import com.firkat.weatherapp.presentation.favourite.FavouriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickSearch: Intent
        data object ClickToFavourite: Intent

        data class CityItemClicked (val city: City): Intent
    }

    data class State(
        val cityItems: List<CityItem>
    ) {
        data class CityItem(
            val city: City,
            val weatherState: State.WeatherState
        )
        sealed interface WeatherState {
            data object Initial: WeatherState
            object Loading: WeatherState
            object Error: WeatherState
            data class Loaded(val temp: Float, val iconUrl: String): WeatherState
        }
    }

    sealed interface Label {
        data object ClickSearch: Label
        data object ClickToFavourite: Label

        data class CityItemClicked (val city: City): Label
    }
}

class FavouriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase
) {

    fun create(): FavouriteStore =
        object : FavouriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavouriteStore",
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavouriteCitiesIsLoaded(val cities: List<City>) : Action
    }

    private sealed interface Msg {
        data class FavouriteCitiesIsLoaded(val cities: List<City>) : Msg

        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Float,
            val conditionIconUrl: String
        ) : Msg

        data class WeatherLoadingError(val cityId: Int): Msg

        data class WeatherIsLoading(val cityId: Int): Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase.invoke().collect {
                    dispatch(Action.FavouriteCitiesIsLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.CityItemClicked -> {
                    publish(Label.CityItemClicked(intent.city))
                }
                Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }
                Intent.ClickToFavourite -> {
                    publish(Label.ClickToFavourite)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteCitiesIsLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCitiesIsLoaded(cities))
                    cities.forEach { city ->
                        scope.launch {
                            loadWeatherForCity(city.id)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(cityId: Int) {
            dispatch(Msg.WeatherIsLoading(cityId))
            try {
                val weather = getCurrentWeatherUseCase.invoke(cityId)
                dispatch(Msg.WeatherLoaded(
                    cityId = cityId,
                    tempC = weather.tempC,
                    conditionIconUrl = weather.conditionUrl
                ))
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(cityId))
            }

        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.FavouriteCitiesIsLoaded -> {
                    copy(
                        cityItems = msg.cities.map {
                            State.CityItem(
                                city = it,
                                weatherState = State.WeatherState.Initial
                            )
                        }
                    )
                }
                is Msg.WeatherIsLoading -> {
                    copy(
                        cityItems = cityItems.map {
                            if (it.city.id == msg.cityId) {
                                it.copy(weatherState = State.WeatherState.Loading)
                            } else {
                                it
                            }
                        }
                    )
                }
                is Msg.WeatherLoaded -> {
                    copy(
                        cityItems = cityItems.map {
                            if (it.city.id == msg.cityId) {
                                it.copy(
                                    weatherState = State.WeatherState.Loaded(
                                        temp = msg.tempC,
                                        iconUrl = msg.conditionIconUrl
                                    )
                                )
                            } else {
                                it
                            }
                        }
                    )
                }
                is Msg.WeatherLoadingError -> {
                    copy(
                        cityItems = cityItems.map {
                            if (it.city.id == msg.cityId) {
                                it.copy(weatherState = State.WeatherState.Error)
                            } else {
                                it
                            }
                        }
                    )
                }
            }
    }
}
