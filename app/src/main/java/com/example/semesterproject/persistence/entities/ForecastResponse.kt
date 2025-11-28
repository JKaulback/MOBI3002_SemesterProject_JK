package com.example.semesterproject.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_responses")
data class ForecastResponse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val location: Location,
    val current: Current
)
