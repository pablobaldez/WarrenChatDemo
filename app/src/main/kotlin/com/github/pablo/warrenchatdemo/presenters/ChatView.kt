package com.github.pablo.warrenchatdemo.presenters

import com.github.pablo.warrenchatdemo.model.InputMask

interface ChatView {

    fun addMessage(message: ChatMessage)

    fun showInputArea(inputMask: InputMask)

    fun hideInputArea()

    fun showErrorMessage()

}