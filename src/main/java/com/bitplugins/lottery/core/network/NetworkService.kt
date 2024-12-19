package com.bitplugins.lottery.core.network

import com.bitplugins.lottery.core.network.di.FileType
import com.bitplugins.lottery.core.network.response.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import java.io.File

interface NetworkService {
    /**
     * Método GET para buscar dados.
     * @param url URL do recurso.
     * @return ResponseWrapper contendo o resultado da chamada.
     */
    suspend fun get(url: String): Flow<ResponseWrapper<String>>

    /**
     * Método POST para enviar dados.
     * @param url URL do recurso.
     * @param data Dados a serem enviados.
     * @return ResponseWrapper contendo o resultado da chamada.
     */
    suspend fun post(url: String, data: String): Flow<ResponseWrapper<String>>

    /**
     * Método para baixar arquivos.
     * @param url URL do arquivo.
     * @param destination Caminho onde o arquivo será salvo.
     * @return ResponseWrapper contendo sucesso ou erro.
     */
    suspend fun download(
        url: String,
        destination: String? = null,
        fileName: String,
        fileType: FileType
    ): Flow<ResponseWrapper<File>>
}
