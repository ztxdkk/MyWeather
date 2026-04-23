package com.example.myweather.network

import com.example.myweather.bean.CitySearchResponse
import com.example.myweather.bean.NowWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("city/lookup")
    suspend fun searchCity(
        @Query("location") query: String,
        @Query("key") key: String
    ): CitySearchResponse

    @GET("weather/now")
    suspend fun getNowWeather(
        @Query("location") locationId: String,
        @Query("key") key: String
    ): NowWeatherResponse
}