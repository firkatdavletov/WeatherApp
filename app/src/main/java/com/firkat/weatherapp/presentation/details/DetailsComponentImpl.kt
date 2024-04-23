package com.firkat.weatherapp.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.firkat.weatherapp.domain.entity.City
import com.firkat.weatherapp.presentation.extensions.componentScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsComponentImpl @Inject constructor(
    private val city: City,
    private val factory: DetailsStoreFactory,
    private val onBackClicked: () -> Unit,
    componentContext: ComponentContext
) : DetailsComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { factory.create(city) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.ClickBack -> {
                        onBackClicked()
                    }
                }
            }
        }
    }
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun onClickChangeFavouriteStatus() {
        store.accept(DetailsStore.Intent.ClickChangeFavouriteStatus)
    }

}