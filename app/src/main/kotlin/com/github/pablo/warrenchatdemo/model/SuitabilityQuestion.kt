package com.github.pablo.warrenchatdemo.model

class SuitabilityQuestion(
        val id: String?,
        val messages: ArrayList<Message>?,
        private val buttons: ArrayList<AvailableButtonAnswer>?,
        private val inputs: ArrayList<Input>?,
        private val responses: ArrayList<String>?
) {

    fun getButtonTitle(index: Int) = buttons?.getOrNull(index)?.labelTitle

    fun getButtonValue(index: Int) = buttons?.getOrNull(index)?.value

    fun getMask() = inputs?.getOrNull(0)?.mask

    fun getResponse() = responses?.getOrNull(0)

    fun getMessageValue(index: Int) = messages?.getOrNull(index)?.value

}