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
        if (!BitCore.context.fileUtils.exists(LOG_DIRECTORY)) {
            BitCore.context.fileUtils.createDirectory(LOG_DIRECTORY)
        }

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

        fun padToLength(text: String, length: Int): String {
            val padding = length - text.length - 2
            return " " + text + " ".repeat(padding)
        }

        logBuilder.append("-".repeat(boxWidth) + "\n")

        logBuilder.append(padToLength("⚡ BitPlugins Exception Logger ⚡", boxWidth) + "\n")
        logBuilder.append(padToLength("🚨 Exception Log 🚨", boxWidth) + "\n")
        logBuilder.append("-".repeat(boxWidth) + "\n")

        logBuilder.append(padToLength("📅 Date: $timestamp", boxWidth) + "\n")
        logBuilder.append(padToLength("❌ Exception Message: ${exception.message}", boxWidth) + "\n")
        logBuilder.append(padToLength("🧐 Exception Cause: ${exception.cause}", boxWidth) + "\n")
        logBuilder.append("-".repeat(boxWidth) + "\n")

        customMessage?.let {
            logBuilder.append(padToLength("💬 Custom Message: $it", boxWidth) + "\n")
            logBuilder.append("-".repeat(boxWidth) + "\n")
        }

        // Detalhes do stack trace, alinhando corretamente
        exception.stackTrace.firstOrNull()?.let { stackTraceElement ->
            logBuilder.append(padToLength("🔍 Class: ${stackTraceElement.className}", boxWidth) + "\n")
            logBuilder.append(padToLength("📝 Method: ${stackTraceElement.methodName}", boxWidth) + "\n")
            logBuilder.append(padToLength("📍 Line: ${stackTraceElement.lineNumber}", boxWidth) + "\n")
        }

        val stackTraceTitle = "🗒️ STACK TRACE"
        val titlePadding = (boxWidth - stackTraceTitle.length - 2) / 2
        logBuilder.append("-".repeat(titlePadding) + " $stackTraceTitle " + "-".repeat(titlePadding) + "\n")

        exception.stackTrace.forEach {
            logBuilder.append(padToLength(it.toString(), boxWidth) + "\n")
        }

        logBuilder.append("-".repeat(boxWidth) + "\n")

        return logBuilder.toString()
    }


}