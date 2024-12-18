package com.bitplugins.lottery.core.util

import org.bukkit.Bukkit
import java.text.SimpleDateFormat
import java.util.*

object Logger {

    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    enum class LogLevel {
        INFO,
        WARNING,
        ERROR
    }

    private fun logToConsole(message: String, level: LogLevel) {
        val timestamp = SimpleDateFormat(DATE_FORMAT).format(Date())
        val emoji = when (level) {
            LogLevel.INFO -> "📗 §a"
            LogLevel.WARNING -> "📙 §6"
            LogLevel.ERROR -> "📕 §c"
        }
        val logMessage = "$emoji [$timestamp] [$level] $message"
        val console = Bukkit.getConsoleSender();

        when (level) {
            LogLevel.INFO -> console.sendMessage(logMessage)
            LogLevel.WARNING -> console.sendMessage(logMessage)
            LogLevel.ERROR -> console.sendMessage(logMessage)
        }
    }

    fun info(message: String) {
        logToConsole(message, LogLevel.INFO)
    }

    fun warning(message: String) {
        logToConsole(message, LogLevel.WARNING)
    }

    fun error(message: String) {
        logToConsole(message, LogLevel.ERROR)
    }

    fun logException(exception: Exception, customMessage: String? = null) {
        val errorMessage = customMessage ?: exception.message ?: "Unknown exception."
        error("exception detect: $errorMessage")
        exception.printStackTrace()
    }
}