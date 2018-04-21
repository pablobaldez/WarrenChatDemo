package com.github.pablo.warrenchatdemo.injection

import com.github.pablo.warrenchatdemo.api.MessagesApi
import com.github.pablo.warrenchatdemo.api.build
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideMessagesApi(retrofit: Retrofit): MessagesApi = retrofit.create(MessagesApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder().build("https://dev-api.oiwarren.com/")


}