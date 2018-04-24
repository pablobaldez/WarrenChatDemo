package com.github.pablo.warrenchatdemo.views.chat

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.databinding.VhChatMessageBinding
import com.github.pablo.warrenchatdemo.presenters.ChatMessage

class ChatViewHolder private constructor(private val binding: VhChatMessageBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(chatMessage: ChatMessage, shouldShowIcon: Boolean) {
        binding.chatMessage = chatMessage
        binding.setupIcon(shouldShowIcon)
        binding.executePendingBindings()
    }

    private fun VhChatMessageBinding.setupIcon(shouldShowIcon: Boolean) {
        chatMessage?.fromUser?.let { fromUser ->
            if(shouldShowIcon) {
                messageIcon.visibility = View.VISIBLE
                if(fromUser) {
                    drawUserInitial()
                } else {
                    messageIcon.setImageResource(R.mipmap.ic_launcher_round)
                }
            } else {
                messageIcon.visibility = View.GONE
            }
        }
    }

    private fun VhChatMessageBinding.drawUserInitial() {
        userInitial?.let {
            val drawable = TextDrawable.builder().buildRound(it, Color.BLACK)
            messageIcon.setImageDrawable(drawable)
        }
    }

    companion object {

        var userInitial: String? = "S"

        fun new(inflater: LayoutInflater, parent: ViewGroup): ChatViewHolder{
            val binding = VhChatMessageBinding.inflate(inflater, parent, false)
            return ChatViewHolder(binding)
        }
    }

}