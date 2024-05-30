package com.pp.ppbacked.service

import com.pp.ppbacked.rest.CertificateResponse
import com.pp.ppbacked.rest.CertificateRequest
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
    private val certificatePdfGenerator: CertificatePdfGenerator,
    private val certificateValidator: CertificateValidator
) {

    fun generateAndSaveCert(request: CertificateRequest): CertificateResponse {
        val csvRecordCert = CsvCertificateRecord(
            certName = request.certificateName,
            firstName = request.recipientName,
            lastName = request.recipientLastName,
            email = request.recipientEmail,
            expirationDate = request.expirationDate
        )
        certificateValidator.validateCertificate(csvRecordCert)
        val certificateAsPdf = certificatePdfGenerator.generatePdfBytes(csvRecordCert, request.issuer)
        val pdfChecksum = getChecksum(certificateAsPdf)
        fileHelper.saveFileToResources(certificateAsPdf, pdfChecksum)

        return CertificateResponse(
            checksum = pdfChecksum,
            recipientName = request.recipientName,
            recipientLastName = request.recipientLastName,
            recipientEmail = request.recipientEmail,
            daysValid = calculateDaysValid(request.expirationDate),
            certificateName = request.certificateName,
            issuer = request.issuer
        )
    }

    fun generateAndSaveCertsFromCsv(file: MultipartFile, issuer: String): List<CertificateResponse> {
        val certificates = csvParser.readCsvFile(file);
        val certificatesAsPdfs = certificates.mapIndexed { index, csvRecordCert ->
            certificateValidator.validateCertificate(csvRecordCert, index)
            certificatePdfGenerator.generatePdfBytes(csvRecordCert, issuer)
        }

        return certificates.mapIndexed { index, cert ->
            val checksum = getChecksum(certificatesAsPdfs[index])
            fileHelper.saveFileToResources(certificatesAsPdfs[index], checksum)

            CertificateResponse(
                checksum = checksum,
                recipientName = cert.firstName,
                recipientLastName = cert.lastName,
                recipientEmail = cert.email,
                daysValid = calculateDaysValid(cert.expirationDate),
                certificateName = cert.certName,
                issuer = issuer
            )
        }
    }

    private fun getChecksum(bytes: ByteArray): String {
        val hash: ByteArray = MessageDigest.getInstance("SHA-256").digest(bytes)
        return BigInteger(1, hash).toString(16)
    }

    private fun calculateDaysValid(date: LocalDate) = ChronoUnit.DAYS.between(LocalDate.now(), date)
        .coerceAtLeast(0).toString()
}
