package com.example.semesterproject.api

import android.util.Log
import com.example.semesterproject.models.ForecastResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WeatherRetrofitApi {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val TIMEOUT_DURATION = 30000L

    // Adds api key to weather api requests
    private val apiKeyInterceptor = Interceptor { chain ->
        val httpUrl = chain.request().url().newBuilder()
            .addQueryParameter("key", "5b37a7541f9240c69b8173225251011")
            .build()

        val request = chain.request().newBuilder()
            .url(httpUrl)
            .build()

        Log.d("WeatherApi", "Built request URL: ${request.url()}")

        chain.proceed(request)
    }

    // Logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    // okHttpClient for Retrofit
    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(loggingInterceptor)
        .callTimeout(TIMEOUT_DURATION, TimeUnit.MILLISECONDS)
        .build()

    // Build the Retrofit object
    private fun buildRetrofit() = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Call create() on Retrofit instance w/ WeatherApiService interface
    private val weatherApiService: WeatherApiService = buildRetrofit().create(WeatherApiService::class.java)

    // Add suspend function to hit the forecast.json endpoint
    suspend fun getForecast(q: String, days: Int):
            ForecastResponse = weatherApiService.getForecast(q, days)

}