package com.example.myweather

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.util.Constants
import com.example.myweather.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()

    // 根布局，用于天气变色
    private lateinit var rootLayout: ConstraintLayout

    // 城市ID映射表
    private val cityIdMap = mapOf(
        "北京" to "101010100",
        "上海" to "101020100",
        "广州" to "101280101",
        "深圳" to "101280601",
        "杭州" to "101210101",
        "成都" to "101270101"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 绑定根布局
        rootLayout = binding.rootLayout

        initObservers()
        initListeners()

        // 默认加载北京天气
        viewModel.getNowWeather("101010100", Constants.API_KEY)
    }

    private fun initObservers() {
        viewModel.cityData.observe(this) { cityResponse ->
            cityResponse?.location?.firstOrNull()?.id?.let { locationId ->
                viewModel.getNowWeather(locationId, Constants.API_KEY)
            }
        }

        viewModel.weatherData.observe(this) { weatherResponse ->
            if (weatherResponse != null && weatherResponse.now != null) {
                val weatherInfo = """
城市：${binding.etCityName.text}
温度：${weatherResponse.now.temp}℃
体感温度：${weatherResponse.now.feelsLike}℃
天气：${weatherResponse.now.text}
风向：${weatherResponse.now.windDir} ${weatherResponse.now.windScale}级
                """.trimIndent()
                binding.tvWeatherInfo.text = weatherInfo

                // 自动切换背景色
                setBackgroundByWeather(weatherResponse.now.text)

            } else if (weatherResponse != null) {
                binding.tvWeatherInfo.text = "天气数据为空"
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSearch.isEnabled = !isLoading
        }

        viewModel.errorMsg.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                binding.tvWeatherInfo.text = it
            }
        }
    }

    private fun initListeners() {
        binding.btnSearch.setOnClickListener {
            val cityName = binding.etCityName.text.toString().trim()
            if (!isValidCityName(cityName)) {
                return@setOnClickListener
            }

            val locationId = cityIdMap[cityName]
            if (locationId != null) {
                viewModel.getNowWeather(locationId, Constants.API_KEY)
            } else {
                viewModel.searchCity(cityName, Constants.API_KEY)
            }
        }
    }

    private fun isValidCityName(cityName: String): Boolean {
        if (cityName.isEmpty()) {
            Toast.makeText(this, "请输入城市名", Toast.LENGTH_SHORT).show()
            return false
        }
        val regex = "^[\\u4e00-\\u9fa5a-zA-Z\\s]+$".toRegex()
        if (!cityName.matches(regex)) {
            Toast.makeText(this, "城市名包含非法字符", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // ===================== 天气自动变色 =====================
    private fun setBackgroundByWeather(weatherText: String) {
        when {
            weatherText.contains("晴") -> {
                rootLayout.setBackgroundColor(0xFFE3F2FD.toInt())
            }
            weatherText.contains("云") || weatherText.contains("阴") -> {
                rootLayout.setBackgroundColor(0xFFF5F5F5.toInt())
            }
            weatherText.contains("雨") -> {
                rootLayout.setBackgroundColor(0xFFBBDEFB.toInt())
            }
            weatherText.contains("雪") -> {
                rootLayout.setBackgroundColor(0xFFFFFFFF.toInt())
            }
            else -> {
                rootLayout.setBackgroundColor(0xFFF5F7FA.toInt())
            }
        }
    }
}