package com.example.myweather.bean

import com.google.gson.annotations.SerializedName

data class NowWeatherResponse(
    @SerializedName("code") val code: String,
    @SerializedName("now") val now: Now?
)

data class Now(
    @SerializedName("temp") val temp: String,
    @SerializedName("feelsLike") val feelsLike: String,
    @SerializedName("text") val text: String,
    @SerializedName("windDir") val windDir: String,
    @SerializedName("windScale") val windScale: String
)