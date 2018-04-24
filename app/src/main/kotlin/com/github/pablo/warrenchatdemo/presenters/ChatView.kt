package com.github.pablo.warrenchatdemo.presenters

import com.github.pablo.warrenchatdemo.model.InputMask
import java.util.*

interface ChatView {

    fun showUserInitial(userInitial: String)

    fun showMessages(messages: Queue<MessageItem>, firstAnswer: String, secondAnswer: String)

    fun showMessages(messages: Queue<MessageItem>, mask: InputMask)

    fun showUserAnswer(item: MessageItem)

    fun showErrorMessage()

}