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

import androidx.lifecycle.viewModelScope
import com.example.myweather.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    // 城市搜索结果
    private val _cityData = MutableLiveData<CitySearchResponse?>()
    val cityData: LiveData<CitySearchResponse?> = _cityData

    // 天气数据
    private val _weatherData = MutableLiveData<NowWeatherResponse?>()
    val weatherData: LiveData<NowWeatherResponse?> = _weatherData

    // 加载状态
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // 错误信息
    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> = _errorMsg

    // 搜索城市
    fun searchCity(query: String, key: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            val result = repository.searchCity(query, key)
            _isLoading.value = false
            result.onSuccess { response ->
                if (response.code == "200" && !response.location.isNullOrEmpty()) {
                    _cityData.value = response
                } else {
                    _errorMsg.value = "未找到该城市，请检查输入"
                }
            }.onFailure {
                _errorMsg.value = "城市搜索失败: ${it.message}"
            }
        }
    }

    // 获取实时天气
    fun getNowWeather(locationId: String, key: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            val result = repository.getNowWeather(locationId, key)
            _isLoading.value = false
            result.onSuccess {
                _weatherData.value = it
            }.onFailure {
                _errorMsg.value = "天气获取失败: ${it.message}"
            }
        }
    }
}