package com.github.pablo.warrenchatdemo.presenters

import com.github.pablo.warrenchatdemo.model.Message
import com.github.pablo.warrenchatdemo.utils.logD
import java.util.regex.Pattern

object MessageMapper {

    fun map(message: Message) {
        val regex = Regex("\\^\\w+ ")
        val pattern = Pattern.compile("\\^\\w+ ")
        val matcher = pattern.matcher(message.value)
        while (matcher.find()) {
            logD(matcher.group())
        }
        val split = message.value?.split(regex)
        var s = "- "
        split?.forEach {
            s += it
        }
        logD("###finishing ${split?.size} ${matcher.groupCount()} $s")
    }

}