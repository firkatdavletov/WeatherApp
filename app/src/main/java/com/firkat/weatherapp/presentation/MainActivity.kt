package com.firkat.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.firkat.weatherapp.WeatherApp
import com.firkat.weatherapp.data.network.api.ApiFactory
import com.firkat.weatherapp.presentation.root.RootComponentImpl
import com.firkat.weatherapp.presentation.root.RootContent
import com.firkat.weatherapp.presentation.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject lateinit var rootComponentFactory: RootComponentImpl.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApp).applicationComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            RootContent(rootComponent = rootComponentFactory.create(defaultComponentContext()))
        }
    }
}