package com.example.semesterproject.persistence.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_responses")
data class ForecastResponse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @Embedded(prefix = "location_") val location: Location,
    @Embedded(prefix = "current_") val current: Current
)
