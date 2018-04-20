package com.github.pablo.warrenchatdemo.utils

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun Retrofit.Builder.build(baseUrl: String): Retrofit {
    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.addInterceptor(createLogInterceptor())
    return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
}

private fun createLogInterceptor(): Interceptor {
    val logger = HttpLoggingInterceptor.Logger { logD(it) }
    val logging = HttpLoggingInterceptor(logger)
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}