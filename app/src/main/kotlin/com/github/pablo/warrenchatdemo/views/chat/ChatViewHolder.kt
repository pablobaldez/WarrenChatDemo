package com.github.pablo.warrenchatdemo.views.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.pablo.warrenchatdemo.databinding.VhChatMessageBinding
import com.github.pablo.warrenchatdemo.presenters.ChatMessage

class ChatViewHolder private constructor(private val binding: VhChatMessageBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(chatMessage: ChatMessage) {
        binding.chatMessage = chatMessage
        binding.executePendingBindings()
    }

    companion object {
        fun new(inflater: LayoutInflater, parent: ViewGroup): ChatViewHolder{
            val binding = VhChatMessageBinding.inflate(inflater, parent, false)
            return ChatViewHolder(binding)
        }
    }

}