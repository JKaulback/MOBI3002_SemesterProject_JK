package com.example.semesterproject.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.semesterproject.persistence.entities.ForecastResponse

@Dao
interface ForecastDao {

    @Insert
    suspend fun insert(forecastResponse: ForecastResponse)

    @Query("SELECT * FROM forecast_responses")
    suspend fun getForecasts(): List<ForecastResponse>

}