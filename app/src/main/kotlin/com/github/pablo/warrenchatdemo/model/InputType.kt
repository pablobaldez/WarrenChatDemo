package com.github.pablo.warrenchatdemo.model

import com.google.gson.annotations.SerializedName

enum class InputType(

) {

    @SerializedName("string")
    STRING,
    @SerializedName("number")
    NUMBER,
    @SerializedName("email")
    EMAIL

}