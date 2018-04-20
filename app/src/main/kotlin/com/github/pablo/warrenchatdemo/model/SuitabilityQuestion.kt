package com.github.pablo.warrenchatdemo.model

class SuitabilityQuestion(
        val id: String?,
        val messages: ArrayList<Message>?,
        val buttons: ArrayList<AvailableButtonAnswer>?,
        val inputs: ArrayList<Input>?,
        val responses: ArrayList<String>?
)