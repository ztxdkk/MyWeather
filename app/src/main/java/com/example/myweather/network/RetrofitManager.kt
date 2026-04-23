package com.example.myweather.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    // 👉 把这里换成你和风天气控制台「开发者信息」里的API地址
    // 示例：https://ng6r6ywjj3.re.qweatherapi.com/v7/
    private const val BASE_URL = "https://ng6r6ywjj3.re.qweatherapi.com/v7/"

    // 初始化 OkHttpClient，加上日志拦截器，方便调试
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // 初始化 Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}