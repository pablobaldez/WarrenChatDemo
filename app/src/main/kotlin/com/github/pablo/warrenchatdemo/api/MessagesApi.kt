package com.github.pablo.warrenchatdemo.api

import com.github.pablo.warrenchatdemo.model.SuitabilityQuestion
import com.github.pablo.warrenchatdemo.model.UserAnswer
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface MessagesApi {

    @POST("api/v2/conversation/message")
    fun answer(@Body answer: UserAnswer): Single<SuitabilityQuestion>

}