package com.github.pablo.warrenchatdemo.model

class SuitabilityQuestion(
        val id: String? = null,
        val messages: ArrayList<Message>? = null,
        private val buttons: ArrayList<AvailableButtonAnswer>? = null,
        private val inputs: ArrayList<Input>? = null,
        private val responses: ArrayList<String>? = null
) {

    fun getButtonTitle(index: Int) = buttons?.getOrNull(index)?.labelTitle

    fun getButtonValue(index: Int) = buttons?.getOrNull(index)?.value

    fun getMask() = inputs?.getOrNull(0)?.mask

    fun getResponse() = responses?.getOrNull(0)

    fun getMessageValue(index: Int) = messages?.getOrNull(index)?.value

}