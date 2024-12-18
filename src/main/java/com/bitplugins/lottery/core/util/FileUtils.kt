package com.bitplugins.lottery.core.util

import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class FileUtils(private val dataFolder: File) {

    fun exists(path: String): Boolean {
        return File(dataFolder, path).exists()
    }

    // Lê o conteúdo de um arquivo como String
    fun readFileAsString(filePath: String): String {
        return try {
            File(dataFolder, filePath).readText()
        } catch (e: IOException) {
            throw IOException("Failed read: $filePath", e)
        }
    }

    fun writeFile(path: String, content: String) {
        val file = File(dataFolder, path)
        try {
            file.parentFile?.let { createDirectory(it.path) }
            file.writeText(content)
        } catch (e: IOException) {
            println("Failed save this file: ${e.message}")
        }
    }

    fun copyFile(sourcePath: String, destinationPath: String) {
        try {
            File(dataFolder, sourcePath).copyTo(File(dataFolder, destinationPath))
        } catch (e: IOException) {
            throw IOException("Failed copy $sourcePath to $destinationPath", e)
        }
    }

    fun deleteFile(path: String): Boolean {
        return try {
            Files.deleteIfExists(Paths.get(path))
        } catch (e: IOException) {
            throw IOException("Failed delete file: $path", e)
        }
    }

    fun listFilesInDirectory(directoryPath: String): List<String> {
        return try {
            Files.list(Paths.get(directoryPath)).map { it.fileName.toString() }.toList()
        } catch (e: IOException) {
            throw IOException("File to list repository's: $directoryPath", e)
        }
    }

    fun createDirectory(directoryPath: String) {
        try {
            Files.createDirectories(Paths.get(directoryPath))
        } catch (e: IOException) {
            throw IOException("Failed create directory: $directoryPath", e)
        }
    }

    fun isDirectory(path: String): Boolean {
        return Files.isDirectory(Paths.get(path))
    }

    fun isFile(path: String): Boolean {
        return Files.isRegularFile(Paths.get(path))
    }

    fun getFileSize(filePath: String): Long {
        return try {
            Files.size(Paths.get(filePath))
        } catch (e: IOException) {
            throw IOException("Failed get size file: $filePath", e)
        }
    }

    fun zipFile(sourcePath: String, zipFilePath: String) {
        try {
            val fos = FileOutputStream(zipFilePath)
            val zipOut = ZipOutputStream(fos)
            val fileToZip = File(sourcePath)

            zipFileOrDirectory(fileToZip, fileToZip.name, zipOut)
            zipOut.close()
            fos.close()
        } catch (e: IOException) {
            throw IOException("Failed zip the file: $sourcePath", e)
        }
    }

    private fun zipFileOrDirectory(fileToZip: File, fileName: String, zipOut: ZipOutputStream) {
        if (fileToZip.isHidden) return
        if (fileToZip.isDirectory) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(ZipEntry(fileName))
                zipOut.closeEntry()
            } else {
                zipOut.putNextEntry(ZipEntry("$fileName/"))
                zipOut.closeEntry()
            }
            val children = fileToZip.listFiles()
            children?.forEach {
                zipFileOrDirectory(it, "$fileName/${it.name}", zipOut)
            }
            return
        }
        val fis = FileInputStream(fileToZip)
        val zipEntry = ZipEntry(fileName)
        zipOut.putNextEntry(zipEntry)
        val bytes = ByteArray(1024)
        var length: Int
        while (fis.read(bytes).also { length = it } >= 0) {
            zipOut.write(bytes, 0, length)
        }
        fis.close()
    }

    fun unzipFile(zipFilePath: String, destinationDir: String) {
        val destDir = File(destinationDir)
        if (!destDir.exists()) {
            destDir.mkdirs()
        }
        val zipIn = ZipInputStream(FileInputStream(zipFilePath))
        var entry: ZipEntry? = zipIn.nextEntry
        while (entry != null) {
            val filePath = destinationDir + File.separator + entry.name
            if (!entry.isDirectory) {
                extractFile(zipIn, filePath)
            } else {
                val dir = File(filePath)
                dir.mkdirs()
            }
            zipIn.closeEntry()
            entry = zipIn.nextEntry
        }
        zipIn.close()
    }

    private fun extractFile(zipIn: ZipInputStream, filePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(filePath))
        val bytesIn = ByteArray(1024)
        var read: Int
        while (zipIn.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }

    fun copyDefaultFileIfNotExists(resourcePath: String, targetPath: String) {
        val targetFile = File(dataFolder, targetPath)
        if (!targetFile.exists()) {
            val inputStream: InputStream = FileUtils::class.java.classLoader.getResourceAsStream(resourcePath)
                ?: throw IllegalArgumentException("File not found: $resourcePath")

            try {
                Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            } catch (e: IOException) {
                throw IOException("Failed to copy default file: $resourcePath", e)
            }
        }
    }
}