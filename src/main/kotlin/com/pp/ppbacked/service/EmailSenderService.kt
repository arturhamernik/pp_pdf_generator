package com.pp.ppbacked.service

import com.pp.ppbacked.common.ValidationException
import com.pp.ppbacked.rest.CertificateEmailRequest
import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailSenderService(
    private val fileHelper: FileHelper,
    private val emailSender: JavaMailSender,
    private val template: SimpleMailMessage
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun sendSingleEmail(request: CertificateEmailRequest) {
        val message = createMessage(request)
        sendEmail(message, request.checksum, request.recipientEmail)
    }

    fun sendBulkEmail(request: Set<CertificateEmailRequest>) {
        request.forEach {
            sendSingleEmail(it)
        }
    }

    private fun createMessage(request: CertificateEmailRequest): MimeMessage {
        if (!isValidEmail(request.recipientEmail)) {
            throw ValidationException("Invalid email: ${request.recipientEmail} for checksum : ${request.checksum}")
        }

        val certificatePdf = fileHelper.getFileFromResources(request.checksum)
        if (!certificatePdf.exists()) {
            throw ValidationException("Certificate pdf file does not exist for checksum: ${request.checksum}")
        }

        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(request.recipientEmail)
        helper.setSubject(template.subject!!)
        helper.setText(
            template.text!!.format(
                request.recipientName,
                request.recipientLastname,
                request.issuer
            )
        )
        helper.addAttachment(
            "Certificate_${request.recipientName + "_" + request.recipientLastname}.pdf",
            certificatePdf
        )

        return message
    }

    private fun sendEmail(message: MimeMessage, checksum: String, recipientEmail: String) {
        try {
            logger.info("Sending email!")
            emailSender.send(message)
        } catch (ex: MailException) {
            logger.warn(
                "Could not send email to: $recipientEmail for checksum: $checksum",
                ex.message
            )
        }
        logger.info("Email to: $recipientEmail sent successfully!")
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return emailRegex.matches(email)
    }

}
