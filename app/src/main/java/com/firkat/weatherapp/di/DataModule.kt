package com.firkat.weatherapp.di

import android.content.Context
import com.firkat.weatherapp.data.local.db.FavouriteCitiesDao
import com.firkat.weatherapp.data.local.db.FavouriteDatabase
import com.firkat.weatherapp.data.network.api.ApiFactory
import com.firkat.weatherapp.data.network.api.ApiService
import com.firkat.weatherapp.data.repository.FavouriteRepositoryImpl
import com.firkat.weatherapp.data.repository.SearchRepositoryImpl
import com.firkat.weatherapp.data.repository.WeatherRepositoryImpl
import com.firkat.weatherapp.domain.repository.FavouriteRepository
import com.firkat.weatherapp.domain.repository.SearchRepository
import com.firkat.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @ApplicationScope
    @Binds
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @ApplicationScope
    @Binds
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun provideDatabase(context: Context): FavouriteDatabase {
            return FavouriteDatabase.getInstance(context)
        }

        @ApplicationScope
        @Provides
        fun provideCitiesDao(database: FavouriteDatabase): FavouriteCitiesDao {
            return database.getFavouriteCitiesDao()
        }

    }
}