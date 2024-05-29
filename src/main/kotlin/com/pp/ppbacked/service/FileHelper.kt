package com.pp.ppbacked.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream

@Service
class FileHelper(
    @Value("\${certificate.directory}") private val certificateDirectory: String,
) {
    fun saveFileToResources(pdfBytes: ByteArray, filename: String) {
        val filePath: String = certificateDirectory + "${filename}.pdf"
        FileOutputStream(filePath).use { fos -> fos.write(pdfBytes) }
    }

    fun getFileFromResources(filename: String): File {
        val filePath: String = certificateDirectory + "${filename}.pdf"
        return File(filePath)
    }
}
