package com.github.pablo.warrenchatdemo.views.chat

import android.view.ViewGroup
import com.github.pablo.warrenchatdemo.presenters.DelayedMessage
import com.github.pablo.warrenchatdemo.views.base.BaseAdapter
import java.util.*

class ChatAdapter(private val allMessagesTypedListener: () -> Unit) : BaseAdapter<DelayedMessage, QuestionViewHolder>(){

    var messageQueue: Queue<DelayedMessage> = LinkedList()
    set(value) {
        field = value
        addNextMessage()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.new(parent)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        list?.getOrNull(position)?.let {
            holder.typeWriterTextView?.typeFinishListener = {
                addNextMessage()
            }
            holder.bind(it)
        }
    }

    private fun addNextMessage() {
        if(messageQueue.isNotEmpty()) {
            add(messageQueue.poll())
        } else {
            allMessagesTypedListener()
        }
    }
}