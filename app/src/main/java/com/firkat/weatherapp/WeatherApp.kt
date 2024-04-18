package com.firkat.weatherapp

import android.app.Application
import com.firkat.weatherapp.di.ApplicationComponent
import com.firkat.weatherapp.di.DaggerApplicationComponent

class WeatherApp: Application() {
    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent.factory().create(this)
        super.onCreate()
    }
}