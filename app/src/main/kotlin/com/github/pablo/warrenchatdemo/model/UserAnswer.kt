package com.github.pablo.warrenchatdemo.model


class UserAnswer(
    var id: String? = null,
    private val answers: MutableMap<String, Any> = HashMap()
) {
    var context: String? = "suitability"

    fun finish() {
        id = null
        context = null
    }

    fun put(id: String, answer: Any) {
        this.id = id
        answers[id] = answer
    }
}