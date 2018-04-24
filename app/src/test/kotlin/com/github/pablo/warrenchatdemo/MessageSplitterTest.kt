package com.github.pablo.warrenchatdemo

import com.github.pablo.warrenchatdemo.presenters.MessageItem
import com.github.pablo.warrenchatdemo.presenters.DelayedText
import com.github.pablo.warrenchatdemo.presenters.MessageSplitter
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MessageSplitterTest {

    @Test
    fun testEmptyText() {
        assertFailsWith<IllegalArgumentException> {
            MessageSplitter.split(  "", 200)
        }
    }

    @Test
    fun testTextWithoutDelay() {
        val text = "Antes de começarmos, como posso te chamar? :)"
        val delayedMessage = MessageSplitter.split(text, 300L)
        assertDelayedMessage(delayedMessage, DelayedText(text, 300L))
    }

    @Test
    fun testTextWithMoreTextsThanDelays() {
        val text = "Antes de começarmos, ^120 como posso te chamar? :)"
        val delayedMessage = MessageSplitter.split(text, 300L)
        assertDelayedMessage(
                delayedMessage,
                DelayedText("Antes de começarmos,", 120L),
                DelayedText(" como posso te chamar? :)", 300L)
        )
    }

    @Test
    fun testSinglePairOfDelayedText() {
        val text = "Antes de começarmos, como posso te chamar? :) ^200"
        val delayedMessage = MessageSplitter.split(text, 300L)
        assertDelayedMessage(delayedMessage,DelayedText("Antes de começarmos, como posso te chamar? :)", 200L))
    }

    @Test
    fun testManyPairsOfDelayedTexts() {
        val text = "Antes de começarmos, ^200 como posso te chamar? :) ^1000"
        val delayedMessage = MessageSplitter.split(text, 300L)
        assertDelayedMessage(
                delayedMessage,
                DelayedText("Antes de começarmos,", 200),
                DelayedText(" como posso te chamar? :)", 1000)
        )
    }

    @Test
    fun testEraseOnly() {
        val text = "(ops... chat errado) :D ^200 <erase>"
        val delayedMessage = MessageSplitter.split(text, 300L)
        assertDelayedMessage(
                delayedMessage,
                DelayedText("(ops... chat errado) :D", 200, false)
        )
    }

    @Test
    fun testEraseAndTexts() {
        val text = "Você prefere Terror ou Suspense? ^1000 <erase>(ops... chat errado) :D ^200"
        val delayedMessage = MessageSplitter.split(text, 300L)
        assertDelayedMessage(
                delayedMessage,
                DelayedText("Você prefere Terror ou Suspense?", 1000, false),
                DelayedText("(ops... chat errado) :D", 200, true)
        )
    }

    private fun assertDelayedMessage(
            messageItem: MessageItem,
            vararg expectedParts: DelayedText) {

        assertEquals(expectedParts.size, messageItem.parts.size)
        messageItem.parts.forEachIndexed { index, part ->
            assertEquals(expectedParts[index].text, part.text)
            assertEquals(expectedParts[index].delay, part.delay)
            assertEquals(expectedParts[index].actionWrite, part.actionWrite)
        }
    }

}