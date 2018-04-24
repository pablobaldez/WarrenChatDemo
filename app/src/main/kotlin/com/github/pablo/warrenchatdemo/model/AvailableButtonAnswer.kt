package com.github.pablo.warrenchatdemo.model

class AvailableButtonAnswer(
        val value: String?,
        private val label: Label?
) {
    val labelTitle: String? get() = label?.title
}