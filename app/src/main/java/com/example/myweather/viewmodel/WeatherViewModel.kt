package com.example.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweather.bean.CitySearchResponse
import com.example.myweather.bean.NowWeatherResponse
import com.example.myweather.network.RetrofitManager
import com.example.myweather.network.WeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    private val weatherApi: WeatherApi = RetrofitManager.create(WeatherApi::class.java)

    // 城市搜索结果
    private val _cityData = MutableLiveData<CitySearchResponse?>()
    val cityData: LiveData<CitySearchResponse?> = _cityData

    // 天气数据
    private val _weatherData = MutableLiveData<NowWeatherResponse?>()
    val weatherData: LiveData<NowWeatherResponse?> = _weatherData

    // 搜索城市
    fun searchCity(query: String, key: String) {
        weatherApi.searchCity(query, key).enqueue(object : Callback<CitySearchResponse> {
            override fun onResponse(
                call: Call<CitySearchResponse>,
                response: Response<CitySearchResponse>
            ) {
                _cityData.postValue(response.body())
            }

            override fun onFailure(call: Call<CitySearchResponse>, t: Throwable) {
                _cityData.postValue(null)
            }
        })
    }

    // 获取实时天气
    fun getNowWeather(locationId: String, key: String) {
        weatherApi.getNowWeather(locationId, key).enqueue(object : Callback<NowWeatherResponse> {
            override fun onResponse(
                call: Call<NowWeatherResponse>,
                response: Response<NowWeatherResponse>
            ) {
                _weatherData.postValue(response.body())
            }

            override fun onFailure(call: Call<NowWeatherResponse>, t: Throwable) {
                _weatherData.postValue(null)
            }
        })
    }
}