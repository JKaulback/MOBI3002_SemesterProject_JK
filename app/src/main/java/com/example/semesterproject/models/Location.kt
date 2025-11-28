package com.example.semesterproject.models

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("region") val region: String = "",
    @SerializedName("country") val country: String = "",
    @SerializedName("lat") val lat: Double = 0.0,
    @SerializedName("lon") val lon: Double = 0.0,
    @SerializedName("localtime") val localtime: String = ""
)
