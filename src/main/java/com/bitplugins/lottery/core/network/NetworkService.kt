package com.bitplugins.lottery.core.network

import com.bitplugins.lottery.core.network.response.ResponseWrapper
import java.io.File

interface NetworkService {
    /**
     * Método GET para buscar dados.
     * @param url URL do recurso.
     * @return ResponseWrapper contendo o resultado da chamada.
     */
    fun get(url: String): ResponseWrapper<String>

    /**
     * Método POST para enviar dados.
     * @param url URL do recurso.
     * @param data Dados a serem enviados.
     * @return ResponseWrapper contendo o resultado da chamada.
     */
    fun post(url: String, data: String): ResponseWrapper<String>

    /**
     * Método para baixar arquivos.
     * @param url URL do arquivo.
     * @param destination Caminho onde o arquivo será salvo.
     * @return ResponseWrapper contendo sucesso ou erro.
     */
    fun downloadFile(url: String, destination: String): ResponseWrapper<File>
}
