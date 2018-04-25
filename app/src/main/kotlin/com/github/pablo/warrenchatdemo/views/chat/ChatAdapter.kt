package com.github.pablo.warrenchatdemo.views.chat

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.github.pablo.warrenchatdemo.presenters.MessageItem
import com.github.pablo.warrenchatdemo.views.base.blink
import java.util.*

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var list: MutableList<MessageItem> = ArrayList()
    var allMessagesTypedListener: (() -> Unit)? = null
    var messageQueue: Queue<MessageItem> = LinkedList()
    set(value) {
        field = value
        addNextMessage()
    }

    override fun getItemCount(): Int {
        return if(list.isEmpty()) {
            1//blinked item
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = list.getOrNull(position)
        return if(item?.parts != null || item?.finalMessage != null || list.isEmpty()) {
            QUESTION_TYPE
        } else {
            ANSWER_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            QUESTION_TYPE -> QuestionViewHolder.new(parent)
            ANSWER_TYPE -> UserAnswerViewHolder.new(parent)
            else -> throw IllegalArgumentException("invalid view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list.getOrNull(position)
        if(item != null) {
            when (holder) {
                is UserAnswerViewHolder -> holder.bind(item)
                is QuestionViewHolder -> holder.bind(item)
            }
        } else if(list.isEmpty() && holder is QuestionViewHolder) {
            holder.iconImageView?.blink()
        }
    }

    private fun QuestionViewHolder.bind(item: MessageItem) {
        bind(item) {
            addNextMessage()
        }
    }

    private fun UserAnswerViewHolder.bind(item: MessageItem) {
        item.answer?.let { answer ->
            bind(answer)
        }
    }

    private fun addNextMessage() {
        if(messageQueue.isNotEmpty()) {
            add(messageQueue.poll())
        } else {
            allMessagesTypedListener?.invoke()
        }
    }

    fun add(item: MessageItem) {
        list.add(item)
        if(list.size == 1) {
            notifyItemChanged(0)
        } else {
            notifyItemInserted(list.size)
        }

    }

    companion object {
        private const val QUESTION_TYPE = 0
        private const val ANSWER_TYPE = 1
    }
}