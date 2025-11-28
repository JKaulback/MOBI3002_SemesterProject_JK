package com.example.semesterproject.models

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("last_updated") val last_updated: String = "",
    @SerializedName("temp_c") val temp_c: Double = 0.0,
    @SerializedName("temp_f") val temp_f: Double = 0.0,
    @SerializedName("wind_mph") val wind_mph: Double = 0.0,
    @SerializedName("wind_kph") val wind_kph: Double = 0.0,
    @SerializedName("wind_dir") val wind_dir: String = ""
)
