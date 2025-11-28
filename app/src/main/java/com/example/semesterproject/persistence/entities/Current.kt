package com.example.semesterproject.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currents")
data class Current(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val wind_mph: Double,
    val wind_kph: Double,
    val wind_dir: String
)
