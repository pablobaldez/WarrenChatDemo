package com.github.pablo.warrenchatdemo.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import java.util.*


class TypeWriterTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : AppCompatTextView(context, attrs, defStyleAttr){

    private var isRunning = false
    private val mTypeSpeed: Long = 80
    private val mDeleteSpeed: Long = 50
    private val mPauseDelay: Long = 1000

    private val mRunnableQueue: Queue<Repeater> = LinkedList()

    private val mRunNextRunnable = Runnable { runNext() }


    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    fun type(text: CharSequence): TypeWriterTextView {
        return type(text, mTypeSpeed)
    }

    fun type(text: CharSequence, speed: Long): TypeWriterTextView {
        mRunnableQueue.add(TextAdder(text, speed, mRunNextRunnable))
        if (!isRunning) runNext()
        return this
    }

    fun erase(text: CharSequence): TypeWriterTextView {
        return erase(text, mDeleteSpeed)
    }

    fun erase(text: CharSequence, speed: Long): TypeWriterTextView {
        mRunnableQueue.add(TextRemover(text, speed, mRunNextRunnable))

        if (!isRunning) runNext()

        return this
    }

    fun pause(millis: Long): TypeWriterTextView {
        mRunnableQueue.add(TypePauser(millis, mRunNextRunnable))

        if (!isRunning) runNext()

        return this
    }

    fun pause(): TypeWriterTextView {
        return pause(mPauseDelay)
    }

    private fun runNext() {
        isRunning = true
        val next = mRunnableQueue.poll()

        if (next == null) {
            isRunning = false
            return
        }

        next.run()
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

    private inner class TextAdder(private var textToAdd: CharSequence, speed: Long, doneRunnable: Runnable) : Repeater(doneRunnable, speed) {

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

    private inner class TextRemover(private var textToRemove: CharSequence, speed: Long, doneRunnable: Runnable)
        : Repeater(doneRunnable, speed) {

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