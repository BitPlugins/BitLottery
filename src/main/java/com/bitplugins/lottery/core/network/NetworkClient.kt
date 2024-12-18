package com.bitplugins.lottery.core.network

import com.bitplugins.lottery.core.network.response.ResponseWrapper
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class NetworkClient : NetworkService {

    override fun get(url: String): ResponseWrapper<String> {
        return try {
            val connection = createConnection(url, "GET")
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            ResponseWrapper.Success(response)
        } catch (e: Exception) {
            ResponseWrapper.Failure(e.message ?: "Unknown error")
        }
    }

    override fun post(url: String, data: String): ResponseWrapper<String> {
        return try {
            val connection = createConnection(url, "POST")
            connection.outputStream.use { os ->
                val writer = OutputStreamWriter(os, Charsets.UTF_8)
                writer.write(data)
                writer.flush()
            }
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            ResponseWrapper.Success(response)
        } catch (e: Exception) {
            ResponseWrapper.Failure(e.message ?: "Unknown error")
        }
    }

    override fun downloadFile(url: String, destination: String): ResponseWrapper<File> {
        return try {
            val connection = createConnection(url, "GET")
            val destinationFile = File(destination)
            connection.inputStream.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            ResponseWrapper.Success(destinationFile)
        } catch (e: Exception) {
            ResponseWrapper.Failure(e.message ?: "Download error")
        }
    }

    private fun createConnection(url: String, method: String): HttpURLConnection {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        if (method == "POST") connection.doOutput = true
        return connection
    }
}
