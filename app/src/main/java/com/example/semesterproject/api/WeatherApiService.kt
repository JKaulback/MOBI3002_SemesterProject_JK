package com.example.semesterproject.api

import com.example.semesterproject.models.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("/forecast.json")
    suspend fun getForecast(
        @Query("q") q: String,
        @Query("days") days: Int
    ): ForecastResponse
}