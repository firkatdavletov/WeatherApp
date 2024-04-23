package com.firkat.weatherapp.presentation.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.firkat.weatherapp.presentation.details.DetailsComponent
import com.firkat.weatherapp.presentation.favourite.FavouriteComponent
import com.firkat.weatherapp.presentation.search.SearchComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    sealed class Child {
        data class Favourite(val component: FavouriteComponent): Child()
        data class Search(val component: SearchComponent): Child()
        data class Details(val component: DetailsComponent): Child()
    }
}