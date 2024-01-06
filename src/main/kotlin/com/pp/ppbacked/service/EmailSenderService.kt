package com.pp.ppbacked.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.pp.ppbacked.rest.CertificateEmailRequest
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class EmailSenderService(
    private val fileHelper: FileHelper,
    private val emailSender: JavaMailSender,
    private val template: SimpleMailMessage
) {

    fun sendSingleEmail(file: MultipartFile, emailRequestString: String) {
        val mapper = ObjectMapper()
        val emailRequest: CertificateEmailRequest = mapper.readValue(emailRequestString, CertificateEmailRequest::class.java)
        fileHelper.saveFileToResources(file.bytes, emailRequest.checksum)

        sendEmail(createMessage(emailRequest))
    }

    fun sendBulkEmail(emailRequests: Array<CertificateEmailRequest>) {
        emailRequests.forEach { request ->
               sendEmail(createMessage(request))
        }
    }

    private fun createMessage(emailRequest: CertificateEmailRequest): MimeMessage {
        val file = fileHelper.getFileFromResources(emailRequest.checksum)
        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(emailRequest.recipientEmail)
        helper.setSubject(template.subject!!)
        helper.setText(template.text!!.format(
            emailRequest.recipientName,
            emailRequest.recipientSurname,
            emailRequest.issuer))
        helper.addAttachment(
            "Certificate_${emailRequest.recipientName + "_"
                    + emailRequest.recipientSurname}.pdf",
            file)

        return message
    }

    private fun sendEmail(message: MimeMessage) {
        emailSender.send(message)
    }
}
