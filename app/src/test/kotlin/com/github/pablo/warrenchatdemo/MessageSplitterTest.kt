package com.github.pablo.warrenchatdemo

import com.github.pablo.warrenchatdemo.presenters.DelayedMessage
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
    fun testInvalidDelay() {
        val text = "Antes de começarmos, como posso te chamar? :) ^300x"
        assertFailsWith<NumberFormatException> {
            MessageSplitter.split(text, 200)
        }
    }

    @Test
    fun testTextWithOnlyDelay() {
        val text = "^1000"
        assertFailsWith<IllegalArgumentException> {
            MessageSplitter.split(text, 200)
        }
    }

    @Test
    fun testTextWithMoreDelaysThanTexts() {
        val text = "Antes de começarmos, ^120 ^300 como posso te chamar? :)"
        assertFailsWith<IllegalArgumentException> {
            MessageSplitter.split(text, 100)
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
        assertDelayedMessage(delayedMessage,DelayedText(text, 200L))
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
        val text = "<erase>(ops... chat errado) :D ^200"
        val delayedMessage = MessageSplitter.split(text, 300L)
        assertDelayedMessage(
                delayedMessage,
                DelayedText("(ops... chat errado) :D", 200, false),
                DelayedText("(ops... chat errado) :D", 200, true)
        )
    }

    @Test
    fun testEraseAndTexts() {
        val text = "Você prefere Terror ou Suspense? ^1000 <erase>(ops... chat errado) :D ^200"
        val delayedMessage = MessageSplitter.split(text, 300L)
        assertDelayedMessage(
                delayedMessage,
                DelayedText("Você prefere Terror ou Suspense?", 1000, true),
                DelayedText(" (ops... chat errado) :D", 200, false),
                DelayedText(" (ops... chat errado) :D", 200, true)
        )
    }

    @Test
    fun testEraseWithoutDelay() {
        val text = "<erase>(ops... chat errado) :D"
        val delayedMessage = MessageSplitter.split(text, 300)
        assertDelayedMessage(
                delayedMessage,
                DelayedText("(ops... chat errado) :D", 300, false),
                DelayedText("(ops... chat errado) :D", 300, true)
        )
    }

    @Test
    fun testStartingWithErasing() {
        val text = "<erase>(ops... chat errado) :D ^200 Você prefere Terror ou Suspense? ^1000"
        val delayedMessage = MessageSplitter.split(text, 300)
        assertDelayedMessage(
                delayedMessage,
                DelayedText("(ops... chat errado) :D", 200, false),
                DelayedText("(ops... chat errado) :D", 200, true),
                DelayedText(" Você prefere Terror ou Suspense?", 1000, true)
        )
    }

    @Test
    fun testEraseAtMiddle() {
        val text = "Você prefere Terror ou Suspense? ^1000  <erase>(ops... chat errado) :D ^200 Desculpe ^900"
        val delayedMessage = MessageSplitter.split(text, 300)
        assertDelayedMessage(
                delayedMessage,
                DelayedText("Você prefere Terror ou Suspense?", 1000, true),
                DelayedText(" (ops... chat errado) :D", 200, false),
                DelayedText(" (ops... chat errado) :D", 200, true),
                DelayedText(" Desculpe", 900, true)
        )
    }

    private fun assertDelayedMessage(
            delayedMessage: DelayedMessage,
            vararg expectedParts: DelayedText) {

        assertEquals(expectedParts.size, delayedMessage.parts.size)
        delayedMessage.parts.forEachIndexed { index, part ->
            assertEquals(expectedParts[index].text, part.text)
            assertEquals(expectedParts[index].delay, part.delay)
            assertEquals(expectedParts[index].actionWrite, part.actionWrite)
        }
    }

}