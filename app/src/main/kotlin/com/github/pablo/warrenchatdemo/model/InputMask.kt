package com.github.pablo.warrenchatdemo.model

import com.google.gson.annotations.SerializedName

enum class InputMask {

    @SerializedName("currency")
    CURRENCY,
    @SerializedName("integer")
    INTEGER,
    @SerializedName("email")
    EMAIL,
    @SerializedName("name")
    NAME

}