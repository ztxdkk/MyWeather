package com.example.myweather.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.myweather.util.Constants

object RetrofitManager {
    // 初始化 OkHttpClient，加上日志拦截器，方便调试
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // 默认使用天气数据接口地址
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 地理位置接口可能需要不同的 BaseURL
    private val geoRetrofit = Retrofit.Builder()
        .baseUrl(Constants.GEO_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    fun <T> createGeo(serviceClass: Class<T>): T {
        return geoRetrofit.create(serviceClass)
    }
}