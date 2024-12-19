package com.bitplugins.lottery.core.network

import com.bitplugins.lottery.core.BitCore
import com.bitplugins.lottery.core.network.di.FileType
import com.bitplugins.lottery.core.network.response.ResponseWrapper
import com.bitplugins.lottery.core.util.ExceptionLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class NetworkClient : NetworkService {

    override suspend fun get(url: String): Flow<ResponseWrapper<String>> = flow {
        emit(ResponseWrapper.Loading())
        val result = executeRequest(url, "GET") { connection ->
            connection.inputStream.bufferedReader().use { it.readText() }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun post(url: String, data: String): Flow<ResponseWrapper<String>> = flow {
        emit(ResponseWrapper.Loading())
        val result = executeRequest(url, "POST") { connection ->
            connection.outputStream.use { os ->
                os.writer(Charsets.UTF_8).apply {
                    write(data)
                    flush()
                }
            }
            connection.inputStream.bufferedReader().use { it.readText() }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun download(
        url: String,
        destination: String?,
        fileName: String,
        fileType: FileType
    ): Flow<ResponseWrapper<File>> = flow {
        emit(ResponseWrapper.Loading())
        try {
            val destinationFile = prepareFile(destination, fileName, fileType)
            val connection = createConnection(url, "GET")

            connection.inputStream.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    val contentLength = connection.contentLength
                    var totalBytesRead = 0
                    var bytesRead: Int

                    while (input.read(buffer).also { bytesRead = it } >= 0) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        val progress = if(contentLength > 0) (totalBytesRead.toDouble() / contentLength * 100).toInt()
                        else 0
                        emit(ResponseWrapper.Loading(progress))
                    }
                }
            }
            emit(ResponseWrapper.Success(destinationFile))
        } catch (e: Exception) {
            ExceptionLogger.logException(e)
            emit(ResponseWrapper.Failure(e.message ?: "Error downloading file"))
        }
    }.flowOn(Dispatchers.IO)

    private inline fun <T> executeRequest(
        url: String,
        method: String,
        crossinline block: (HttpURLConnection) -> T
    ): ResponseWrapper<T> {
        return try {
            val connection = createConnection(url, method)
            val result = block(connection)
            ResponseWrapper.Success(result)
        } catch (e: Exception) {
            ExceptionLogger.logException(e)
            ResponseWrapper.Failure(e.message ?: "Unknown error")
        }
    }

    private fun prepareFile(destination: String?, fileName: String, fileType: FileType): File {
        val filePath = if (!destination.isNullOrEmpty() && !destination.contains(".") && !destination.endsWith("/")) {
            "$destination/$fileName${fileType.extension}"
        } else {
            "$fileName${fileType.extension}"
        }
        val file = File(BitCore.context.dataFolder, filePath)
        file.parentFile?.mkdirs()
        return file
    }

    private fun createConnection(url: String, method: String): HttpURLConnection {
        return (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 5000
            readTimeout = 5000
            if (method == "POST") doOutput = true
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 8192
    }
}
