package com.pp.ppbacked.service

import com.pp.ppbacked.rest.CertificateDto
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class CertificateService(
    private val fileHelper: FileHelper,
    private val csvParser: CsvParser,
    private val certificatePdfGenerator: CertificatePdfGenerator
) {

    fun generate(file: MultipartFile, issuer: String): List<CertificateDto> {
        val certificates = csvParser.readCsvFile(file);
        return generateCertificates(certificates, issuer)
    }

    private fun generateCertificates(
        certificates: List<CsvCertificateRecord>,
        issuer: String
    ): List<CertificateDto> {
        val certificatesAsPdfs = generatePdfBytesList(certificates, issuer)
        return saveCertificatesFiles(certificates, certificatesAsPdfs, issuer)
    }

    private fun generatePdfBytesList(
        certificates: List<CsvCertificateRecord>,
        issuer: String
    ): List<ByteArray> {
        return certificates.map {
            certificatePdfGenerator.generatePdfBytes(it, issuer)
        }
    }

    private fun saveCertificatesFiles(
        certificates: List<CsvCertificateRecord>,
        certificatesAsPdf: List<ByteArray>,
        issuer: String
    ): List<CertificateDto> {
        return certificates.mapIndexed { index, cert ->
            val checksum = getChecksum(certificatesAsPdf[index])
            fileHelper.saveFileToResources(certificatesAsPdf[index], checksum)

            val daysValid = ChronoUnit.DAYS.between(LocalDate.now(), cert.expirationDate)
                .coerceAtLeast(0).toString()

            CertificateDto(
                checksum = checksum,
                recipientName = cert.firstName,
                recipientSurname = cert.lastName,
                recipientEmail = cert.email,
                daysValid = daysValid,
                certName = cert.certName,
                issuer = issuer
            )
        }
    }

    private fun getChecksum(bytes: ByteArray): String {
        val hash: ByteArray = MessageDigest.getInstance("SHA-256").digest(bytes)
        return BigInteger(1, hash).toString(16)
    }
}
