package com.suikajy.openglnote.util

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber
import kotlin.math.min

private const val DEFAULT_TAG = "SK"
private const val MAX_LOG_LENGTH = 4000
// This index is adapt to the Timber version 4.7.1
private const val CALL_STACK_INDEX = 6

@SuppressLint("LogNotTimber")
class AndroidStudioTree : Timber.Tree() {
    private fun wrapLogMessage(message: String): String {
        val stackTrace = Throwable().stackTrace
        check(stackTrace.size > CALL_STACK_INDEX) { "Synthetic stacktrace didn't have enough elements: are you using proguard?" }
        val className = stackTrace[CALL_STACK_INDEX].fileName
        val methodName = stackTrace[CALL_STACK_INDEX].methodName
        val lineNumber = stackTrace[CALL_STACK_INDEX].lineNumber
        return "[$methodName($className:$lineNumber)]--  $message"
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val finalTag = tag ?: DEFAULT_TAG
        val finalMessage = wrapLogMessage(message)
        if (finalMessage.length < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(finalTag, finalMessage)
            } else {
                Log.println(priority, finalTag, finalMessage)
            }
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = finalMessage.length
        while (i < length) {
            var newline = finalMessage.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = min(newline, i + MAX_LOG_LENGTH)
                val part = finalMessage.substring(i, end)
                if (priority == Log.ASSERT) {
                    Log.wtf(finalTag, part)
                } else {
                    Log.println(priority, finalTag, part)
                }
                i = end
            } while (i < newline)
            i++
        }
    }
}