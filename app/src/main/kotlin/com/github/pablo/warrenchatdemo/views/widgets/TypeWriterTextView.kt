package com.github.pablo.warrenchatdemo.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import java.util.*


class TypeWriterTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : AppCompatTextView(context, attrs, defStyleAttr){

    companion object {
        const val TYPE_SPEED = 80L
        const val ERASE_SPEED = 50L
    }

    private var isRunning = false
    private val runnableQueue: Queue<Repeater> = LinkedList()
    private val runNextRunnable = Runnable { runNext() }
    var typeFinishListener: (() -> Unit)? = null

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    fun type(text: CharSequence): TypeWriterTextView {
        runnableQueue.add(TextAdder(text, runNextRunnable))
        if (!isRunning) runNext()
        return this
    }

    fun erase(text: CharSequence): TypeWriterTextView {
        runnableQueue.add(TextRemover(text, runNextRunnable))
        if (!isRunning) runNext()
        return this
    }

    fun pause(millis: Long): TypeWriterTextView {
        runnableQueue.add(TypePauser(millis, runNextRunnable))
        if (!isRunning) runNext()
        return this
    }

    private fun runNext() {
        isRunning = true
        val next = runnableQueue.poll()

        if (next == null) {
            isRunning = false
            typeFinishListener?.invoke()
        } else {
            next.run()
        }
    }

    private abstract inner class Repeater(private val mDoneRunnable: Runnable, private val mDelay: Long)
        : Runnable {
        protected var mHandler = Handler()

        protected fun done() {
            mDoneRunnable.run()
        }

        protected fun delayAndRepeat() {
            mHandler.postDelayed(this, mDelay)
        }
    }

    private inner class TextAdder(private var textToAdd: CharSequence, doneRunnable: Runnable) : Repeater(doneRunnable, TYPE_SPEED) {

        @SuppressLint("SetTextI18n")
        override fun run() {
            if (textToAdd.isEmpty()) {
                done()
                return
            }
            val first = textToAdd[0]
            textToAdd = textToAdd.subSequence(1, textToAdd.length)
            val text = text

            setText("$text$first")
            delayAndRepeat()
        }
    }

    private inner class TextRemover(private var textToRemove: CharSequence, doneRunnable: Runnable)
        : Repeater(doneRunnable, ERASE_SPEED) {

        override fun run() {
            if (textToRemove.isEmpty()) {
                done()
                return
            }

            val last = textToRemove[textToRemove.length - 1]
            textToRemove = textToRemove.subSequence(0, textToRemove.length - 1)

            val text = text

            if (text[text.length - 1] == last) {
                setText(text.subSequence(0, text.length - 1))
            }
            delayAndRepeat()
        }
    }

    private inner class TypePauser(delay: Long, doneRunnable: Runnable) : Repeater(doneRunnable, delay) {

        internal var hasPaused = false

        override fun run() {
            if (hasPaused) {
                done()
                return
            }

            hasPaused = true
            delayAndRepeat()
        }
    }

}