package com.firkat.weatherapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.firkat.weatherapp.presentation.details.DetailsContent
import com.firkat.weatherapp.presentation.favourite.FavouriteContent
import com.firkat.weatherapp.presentation.search.SearchContent
import com.firkat.weatherapp.presentation.ui.theme.WeatherAppTheme

@Composable
fun RootContent(rootComponent: RootComponent) {
    WeatherAppTheme {
        Children(stack = rootComponent.stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(detailsComponent = instance.component)
                }
                is RootComponent.Child.Favourite -> {
                    FavouriteContent(favouriteComponent = instance.component)
                }
                is RootComponent.Child.Search -> {
                    SearchContent(searchComponent = instance.component)
                }
            }
        }
    }
}