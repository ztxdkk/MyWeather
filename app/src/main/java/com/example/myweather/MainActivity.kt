package com.example.myweather

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myweather.bean.NowWeatherResponse
import com.example.myweather.network.RetrofitManager
import com.example.myweather.network.WeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // 你的API Key
    private val API_KEY = "1e2d420b688a42579b50ab818bb8a6c6"

    private lateinit var etCityName: EditText
    private lateinit var btnSearch: Button
    private lateinit var tvWeatherInfo: TextView

    // 城市ID映射表（常用城市）
    private val cityIdMap = mapOf(
        "北京" to "101010100",
        "上海" to "101020100",
        "广州" to "101280101",
        "深圳" to "101280601",
        "杭州" to "101210101"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCityName = findViewById(R.id.et_city_name)
        btnSearch = findViewById(R.id.btn_search)
        tvWeatherInfo = findViewById(R.id.tv_weather_info)

        // 默认加载北京天气
        getNowWeather("101010100")

        btnSearch.setOnClickListener {
            val cityName = etCityName.text.toString().trim()
            if (cityName.isEmpty()) {
                Toast.makeText(this, "请输入城市名", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 从映射表获取城市ID
            val locationId = cityIdMap[cityName]
            if (locationId != null) {
                getNowWeather(locationId)
            } else {
                tvWeatherInfo.text = "该城市暂不支持，请输入：北京/上海/广州/深圳/杭州"
            }
        }
    }

    private fun getNowWeather(locationId: String) {
        val api = RetrofitManager.create(WeatherApi::class.java)
        api.getNowWeather(locationId, API_KEY).enqueue(object : Callback<NowWeatherResponse> {
            override fun onResponse(
                call: Call<NowWeatherResponse>,
                response: Response<NowWeatherResponse>
            ) {
                if (!response.isSuccessful) {
                    tvWeatherInfo.text = "天气查询失败，错误码：${response.code()}"
                    return
                }
                val weatherResponse = response.body()
                if (weatherResponse != null && weatherResponse.now != null) {
                    val weatherInfo = """
城市：${etCityName.text}
温度：${weatherResponse.now.temp}℃
体感温度：${weatherResponse.now.feelsLike}℃
天气：${weatherResponse.now.text}
风向：${weatherResponse.now.windDir} ${weatherResponse.now.windScale}级
                    """.trimIndent()
                    tvWeatherInfo.text = weatherInfo
                } else {
                    tvWeatherInfo.text = "天气数据为空"
                }
            }

            override fun onFailure(call: Call<NowWeatherResponse>, t: Throwable) {
                tvWeatherInfo.text = "请求网络异常：${t.message}"
            }
        })
    }
}