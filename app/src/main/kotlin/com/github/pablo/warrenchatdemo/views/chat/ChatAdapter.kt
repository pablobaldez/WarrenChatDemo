package com.github.pablo.warrenchatdemo.views.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.pablo.warrenchatdemo.presenters.ChatMessage
import com.github.pablo.warrenchatdemo.views.base.BaseAdapter

class ChatAdapter : BaseAdapter<ChatMessage, ChatViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChatViewHolder.new(inflater, parent)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        list?.getOrNull(position)?.let {
            val showIcon = position == 0 || list.previousWasFromOtherSender(position, it.fromUser)
            holder.bind(it, showIcon)
        }
    }

    private fun List<ChatMessage>?.previousWasFromOtherSender(index: Int, fromUser: Boolean): Boolean {
        return this?.getOrNull(index-1)?.fromUser != fromUser
    }

}