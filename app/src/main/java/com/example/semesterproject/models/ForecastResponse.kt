package com.example.semesterproject.models

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: Current
)