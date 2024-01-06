package com.pp.ppbacked.rest

import com.pp.ppbacked.service.EmailSenderService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/emails")
class EmailController(
    private val emailSenderService: EmailSenderService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @PostMapping
    fun send(@RequestPart file: MultipartFile, @RequestPart emailRequest: String) {
        logger.info(emailRequest)
        emailSenderService.sendSingleEmail(file, emailRequest)
    }

    @PostMapping("/bulk")
    fun sendBulk(@RequestBody emailRequests: Array<CertificateEmailRequest>) {
        emailSenderService.sendBulkEmail(emailRequests)
    }
}
