package com.github.pablo.warrenchatdemo.presenters

import com.github.pablo.warrenchatdemo.model.InputMask
import java.util.*

interface ChatView {

    fun showMessages(messages: Queue<DelayedMessage>)

    fun showInputArea(inputMask: InputMask)

    fun hideInputArea()

    fun showErrorMessage()

}