package com.github.pablo.warrenchatdemo.presenters

import java.util.regex.Pattern

/**
 * A file to map a Message class into a list of texts and delays to be shown with animation
 */
object MessageSplitter {

    /**
     * @throws NumberFormatException if the delay is in a wrong format
     * @throws IllegalArgumentException if the message empty or if have more delays than messages
     */
    fun split(message: String, defaultDelayToUse: Long): DelayedMessage {
        if(message.isBlank()) {
            throw IllegalArgumentException("blank message")
        }
        val delays = extractDelays(message)
        val parts = if(delays.isEmpty()) {
            val texts = splitTexts(message)
            joinTextAndDelays(delays, texts, defaultDelayToUse)
        } else {
            arrayListOf(DelayedText(message, defaultDelayToUse))
        }
        return DelayedMessage(parts)
    }

    private fun extractDelays(message: String): List<Long> {
        val pattern = Pattern.compile("\\^\\w+")
        val matcher = pattern.matcher(message)
        val delays = ArrayList<Long>()
        while (matcher.find()) {
            val textDelay = matcher.group()
            val delay = textDelay.replace("^", "").toLong()
            delays.add(delay)
        }
        return delays
    }

    private fun splitTexts(message: String): List<Pair<String, Boolean>> {
        val regex = Regex(" \\^\\w+")
        val split = message.split(regex)
        val texts = ArrayList<Pair<String, Boolean>>()
        split.forEach {

        }
        return texts
    }

    private fun joinTextAndDelays(delays: List<Long>,
                                  textsAndActions: List<Pair<String, Boolean>>,
                                  defaultDelayToUse: Long): List<DelayedText> {
        if(delays.size > textsAndActions.size) {
            throw IllegalArgumentException("invalid format of message")
        }
        val parts = ArrayList<DelayedText>()
        var previousIsErase = false
        textsAndActions.forEachIndexed { index, text ->
            if(previousIsErase) {
                val delay = delays.getOrElse(index - 1, {defaultDelayToUse})
                DelayedText(text.first, delay, true)
            } else {
                val delay = delays.getOrElse(index, {defaultDelayToUse})
                DelayedText(text.first, delay, text.second)
            }
            previousIsErase = !text.second
        }
        return parts
    }

}