package com.github.pablo.warrenchatdemo.presenters

import java.util.regex.Pattern

/**
 * An object to map a Message class into a list of texts and delays to be shown with animation
 */
object MessageSplitter {

    /**
     * @throws NumberFormatException if the delay is in a wrong format
     * @throws IllegalArgumentException if the message empty
     */
    fun split(message: String, defaultDelayToUse: Long): MessageItem {
        if(message.isBlank()) {
            throw IllegalArgumentException("blank message")
        }
        val delays = extractDelays(message)
        val parts = if(delays.isNotEmpty()) {
            val texts = splitTexts(message)
            joinTextAndDelays(delays, texts, defaultDelayToUse)
        } else {
            splitTexts(message).map {
                DelayedText(it.first, defaultDelayToUse, it.second)
            }
        }
        return MessageItem(parts = parts)
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
        val eraseSplit = message.split("<erase>")
        val texts: List<Pair<String, Boolean>>
        if(eraseSplit.size == 1) {
            texts = splitTextsByDelay(message).map { Pair(it, true) }
        } else {
            texts = ArrayList()
            eraseSplit.map { splitTextsByDelay(it) }.forEachIndexed { index, list ->
                val splitLists = if(index == 0) {
                    list.map { Pair(it, false) }
                } else {
                    list.map { Pair(it, true) }
                }
                texts.addAll(splitLists)
            }
        }
        return texts
    }

    private fun splitTextsByDelay(message: String): List<String> {
        val regex = Regex(" \\^\\w+")
        return message.split(regex).filter { it.isNotBlank() }.map { it.replace("  ", " ") }
    }

    private fun joinTextAndDelays(delays: List<Long>,
                                  textsAndActions: List<Pair<String, Boolean>>,
                                  defaultDelayToUse: Long): List<DelayedText> {
        val parts = ArrayList<DelayedText>()
        textsAndActions.forEachIndexed { index, pair ->
            val delay = delays.getOrElse(index, {defaultDelayToUse})
            parts.add(DelayedText(pair.first, delay, pair.second))
        }
        return parts
    }

}