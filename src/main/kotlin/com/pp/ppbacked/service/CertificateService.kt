package com.pp.ppbacked.service

import com.pp.ppbacked.dto.CertificateDto
import com.pp.ppbacked.dto.CsvCertificateRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.FileOutputStream
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.time.LocalDate

@Service
class CertificateService(
    @Value("\${certificate.directory}") private val certificateDirectory: String,
    private val csvParser: CsvParser,
    private val certificatePdfGenerator: CertificatePdfGenerator
) {

    fun generate(
        file: MultipartFile,
        issuer: String
    ) : MutableList<CertificateDto> {
        val certificates = csvParser.readCsvFile(file);
        return generateCertificates(certificates, issuer)
    }

    private fun generateCertificates(
        certificates: List<CsvCertificateRecord>,
        issuer: String
    ): MutableList<CertificateDto> {
        val certificatesAsPdfs = generatePdfBytesList(certificates, issuer)
        return saveCertificatesFiles(certificates, certificatesAsPdfs, issuer)
    }

    private fun generatePdfBytesList(
        certificates: List<CsvCertificateRecord>,
        issuer: String
    ): MutableList<ByteArray> {
        val pdfs = mutableListOf<ByteArray>()
        for (cert in certificates) {
            val pdfBytes = certificatePdfGenerator.generatePdfBytes(cert, issuer)
            pdfs.add(pdfBytes)
        }
        return pdfs
    }

    private fun saveFileToResources(
        pdfBytes: ByteArray,
        filename: String
    ) {
        val filePath: String = certificateDirectory + "${filename}.pdf"
        val newFile: Path = Paths.get(filePath)
        Files.createDirectories(newFile.parent)
        FileOutputStream(filePath).use { fos -> fos.write(pdfBytes) }
    }

    private fun getChecksum(bytes: ByteArray): String {
        val hash: ByteArray = MessageDigest.getInstance("SHA-256").digest(bytes)
        return BigInteger(1, hash).toString(16)
    }

    private fun saveCertificatesFiles(
        certificates: List<CsvCertificateRecord>,
        certificatesAsPdf: List<ByteArray>,
        issuer: String
    ): MutableList<CertificateDto> {
        val list = mutableListOf<CertificateDto>()
        certificates.forEachIndexed { index, it ->
            val checksum = getChecksum(certificatesAsPdf[index])
            saveFileToResources(certificatesAsPdf[index], checksum)
            list.add(
                CertificateDto(
                    checksum,
                    it.firstName,
                    it.lastName,
                    it.email,
                    (it.expirationDate.toEpochDay() - LocalDate.now().toEpochDay()).toString(),
                    it.certName,
                    issuer
                )
            )
        }
        return list
    }
}
