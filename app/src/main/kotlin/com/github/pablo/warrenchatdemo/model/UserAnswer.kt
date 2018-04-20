package com.github.pablo.warrenchatdemo.model


class UserAnswer(
    var id: String? = null,
    private val answers: MutableMap<String, Any> = HashMap()
) {
    @Suppress("unused")
    private val context: String = "suitability"

    fun put(id: String, answer: Any) {
        answers[id] = answer
    }
}