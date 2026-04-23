package com.example.myweather.bean

import com.google.gson.annotations.SerializedName

data class CitySearchResponse(
    @SerializedName("code") val code: String,
    @SerializedName("location") val location: List<CityLocation> = emptyList()
)

data class CityLocation(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)