package com.firkat.weatherapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.firkat.weatherapp.domain.entity.City
import com.firkat.weatherapp.domain.entity.Forecast
import com.firkat.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.firkat.weatherapp.domain.usecase.GetForecastUseCase
import com.firkat.weatherapp.domain.usecase.ObserveFavouriteStateUseCase
import com.firkat.weatherapp.presentation.details.DetailsStore.Intent
import com.firkat.weatherapp.presentation.details.DetailsStore.Label
import com.firkat.weatherapp.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack: Intent
        data object ClickChangeFavouriteStatus: Intent
    }


    data class State(
        val city: City,
        val isFavourite: Boolean,
        val state: ForecastState
    ) {
        sealed interface ForecastState {
            data object Initial: ForecastState

            data object Loading: ForecastState

            data object Error: ForecastState

            data class Loaded(val forecast: Forecast): ForecastState
        }
    }

    sealed interface Label {
        data object ClickBack: Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase,
    private val observeFavouriteStateUseCase: ObserveFavouriteStateUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                city = city,
                isFavourite = false,
                state = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavouriteStatusChanged(val isFavourite: Boolean): Action
        data class ForecastLoaded(val forecast: Forecast): Action
        data object ForecastStartLoading: Action
        data object ForecastLoadingError: Action
    }

    private sealed interface Msg {
        data class FavouriteStatusChanged(val isFavourite: Boolean): Msg
        data class ForecastLoaded(val forecast: Forecast): Msg
        data object ForecastStartLoading: Msg
        data object ForecastLoadingError: Msg
    }

    private inner class BootstrapperImpl (private val city: City): CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeFavouriteStateUseCase(cityId = city.id)
                    .collect {
                        dispatch(Action.FavouriteStatusChanged(it))
                    }
            }

            scope.launch {
                dispatch(Action.ForecastStartLoading)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (th: Throwable) {
                    dispatch(Action.ForecastLoadingError)
                }

            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ClickChangeFavouriteStatus -> {
                    scope.launch {
                        val state = getState()
                        if (state.isFavourite) {
                            changeFavouriteStateUseCase.removeFromFavourite(state.city.id)
                        } else {
                            changeFavouriteStateUseCase.addToFavourite(state.city)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteStatusChanged -> {
                    dispatch(Msg.FavouriteStatusChanged(action.isFavourite))
                }
                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }
                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }
                Action.ForecastStartLoading -> {
                    dispatch(Msg.ForecastStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavouriteStatusChanged -> {
                copy(isFavourite = msg.isFavourite)
            }
            is Msg.ForecastLoaded -> {
                copy(state = State.ForecastState.Loaded(msg.forecast))
            }
            Msg.ForecastLoadingError -> {
                copy(state = State.ForecastState.Error)
            }
            Msg.ForecastStartLoading -> {
                copy(state = State.ForecastState.Loading)
            }
        }
    }
}
