package com.github.pablo.warrenchatdemo.presenters

import com.github.pablo.warrenchatdemo.model.InputMask
import java.util.*

interface ChatView {

    fun showMessages(messages: Queue<DelayedMessage>, firstAnswer: String, secondAnswer: String)

    fun showMessages(messages: Queue<DelayedMessage>, mask: InputMask)

    fun hideAnswerArea()

    fun showErrorMessage()

}