package com.example.myweather.network

import com.example.myweather.bean.CitySearchResponse
import com.example.myweather.bean.NowWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    // 必须加上这个，才能在 ViewModel 里调用 searchCity
    @GET("city/lookup")
    fun searchCity(
        @Query("location") query: String,
        @Query("key") key: String
    ): Call<CitySearchResponse>

    @GET("weather/now")
    fun getNowWeather(
        @Query("location") locationId: String,
        @Query("key") key: String
    ): Call<NowWeatherResponse>
}