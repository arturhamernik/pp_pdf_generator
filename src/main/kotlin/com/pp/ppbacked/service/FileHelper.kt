package com.pp.ppbacked.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class FileHelper(
    @Value("\${certificate.directory}")
    private val certificateDirectory: String,
) {
    fun saveFileToResources(pdfBytes: ByteArray, filename: String) {
        val filePath: String = certificateDirectory + "${filename}.pdf"
        val newFile: Path = Paths.get(filePath)
        Files.createDirectories(newFile.parent)
        FileOutputStream(filePath).use { fos -> fos.write(pdfBytes) }
    }

    fun getFileFromResources(filename: String): File {
        val filePath: String = certificateDirectory + "${filename}.pdf"
        return File(filePath)
    }
}
