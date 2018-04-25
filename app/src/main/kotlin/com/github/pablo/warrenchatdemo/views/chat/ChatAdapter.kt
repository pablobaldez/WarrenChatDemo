package com.github.pablo.warrenchatdemo.views.chat

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.github.pablo.warrenchatdemo.presenters.MessageItem
import com.github.pablo.warrenchatdemo.views.base.BaseAdapter
import java.util.*

class ChatAdapter : BaseAdapter<MessageItem, RecyclerView.ViewHolder>(){

    var messageQueue: Queue<MessageItem> = LinkedList()
    set(value) {
        field = value
        addNextMessage()
    }
    private var allMessagesTypedListener: (() -> Unit)? = null
    private var oneMessageWasTypedListener:(() -> Unit)? = null

    fun setListeners(allMessagesTypedListener: (() -> Unit), oneMessageWasTypedListener:(() -> Unit)) {
        this.allMessagesTypedListener = allMessagesTypedListener
        this.oneMessageWasTypedListener = oneMessageWasTypedListener
    }

    override fun getItemViewType(position: Int): Int {
        val item = list.getOrNull(position)
        return if(item?.parts != null || item?.finalMessage != null) {
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
        when(holder) {
            is QuestionViewHolder -> {
                item?.let {
                    holder.typeWriterTextView?.typeFinishListener = {
                        item.animationFinished = true
                        addNextMessage()
                    }
                    holder.bind(item)
                }
            }
            is UserAnswerViewHolder -> {
                item?.let {
                    it.answer?.let { answer ->
                        holder.bind(answer)
                    }
                }
            }
        }
    }

    private fun addNextMessage() {
        if(messageQueue.isNotEmpty()) {
            add(messageQueue.poll())
            oneMessageWasTypedListener?.invoke()
        } else {
            allMessagesTypedListener?.invoke()
        }
    }

    companion object {

        private const val QUESTION_TYPE = 0
        private const val ANSWER_TYPE = 1
    }
}