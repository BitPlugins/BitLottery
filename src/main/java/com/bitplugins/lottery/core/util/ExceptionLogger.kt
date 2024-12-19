package com.bitplugins.lottery.core.util

import com.bitplugins.lottery.core.BitCore
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

object ExceptionLogger {

    private const val LOG_DIRECTORY = "logs"
    private const val LOG_FILE_PREFIX = "bitplugins_exception_"
    private const val DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss"

    fun logException(exception: Exception, customMessage: String? = null) {
        BitCore.context.fileUtils.createDirectory(LOG_DIRECTORY)

        val timestamp = SimpleDateFormat(DATE_FORMAT).format(Date())
        val logFileName = "$LOG_FILE_PREFIX$timestamp.txt"
        val logFilePath = "$LOG_DIRECTORY/$logFileName"

        val logContent = buildLogContent(timestamp, exception, customMessage)

        BitCore.context.fileUtils.writeFile(logFilePath, logContent)

        Logger.error("exception: $logFilePath")
    }

    private fun buildLogContent(timestamp: String, exception: Exception, customMessage: String?): String {
        val logBuilder = StringBuilder()

        val boxWidth = 120

        logBuilder.append("-".repeat(boxWidth) + "\n")

        logBuilder.append("⚡ BitPlugins Exception Logger ⚡" + "\n")
        logBuilder.append("🚨 Exception Log 🚨" + "\n")
        logBuilder.append("-".repeat(boxWidth) + "\n")

        logBuilder.append("📅 Date: $timestamp\n")
        logBuilder.append("❌ Exception Message: ${exception.message}" + "\n")
        logBuilder.append("🧐 Exception Cause: ${exception.cause}" + "\n")
        logBuilder.append("-".repeat(boxWidth) + "\n")

        customMessage?.let {
            logBuilder.append("💬 Custom Message: $it\n")
            logBuilder.append("-".repeat(boxWidth) + "\n")
        }

        exception.stackTrace.firstOrNull()?.let { stackTraceElement ->
            logBuilder.append("🔍 Class: ${stackTraceElement.className}" + "\n")
            logBuilder.append("📝 Method: ${stackTraceElement.methodName}" + "\n")
            logBuilder.append("📍 Line: ${stackTraceElement.lineNumber}" + "\n")
        }

        val stackTraceTitle = "[ 🗒️ STACK TRACE ]"
        val titlePadding = (boxWidth - stackTraceTitle.length - 2) / 2
        logBuilder.append("-".repeat(titlePadding) + " $stackTraceTitle " + "-".repeat(titlePadding) + "\n")

        exception.stackTrace.forEach {
            logBuilder.append(it.toString() + "\n")
        }

        logBuilder.append("-".repeat(boxWidth) + "\n")

        return logBuilder.toString()
    }


}