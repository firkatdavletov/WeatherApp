package com.firkat.weatherapp.presentation.search

import com.firkat.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {
    val model: StateFlow<SearchStore.State>

    fun changeSearchQuery(query: String)
    fun onClickBack()
    fun onClickSearch()
    fun onClickCity(city: City)
}