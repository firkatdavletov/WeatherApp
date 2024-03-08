package com.firkat.weatherapp.data.network.api

import com.firkat.weatherapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.internal.tls.OkHostnameVerifier
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiFactory {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val WEATHER_KEY = "key"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest.url().newBuilder()
                .addQueryParameter(WEATHER_KEY, BuildConfig.WEATHER_API_KEY)
                .build()
            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: ApiService = retrofit.create()
}