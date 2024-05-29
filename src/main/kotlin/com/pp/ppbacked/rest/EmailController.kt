package com.pp.ppbacked.rest

import com.pp.ppbacked.service.EmailSenderService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/emails")
class EmailController(
    private val emailSenderService: EmailSenderService
) {

    @PostMapping
    fun sendEmail(@RequestBody request: CertificateEmailRequest) {
        emailSenderService.sendSingleEmail(request)
    }

    @PostMapping("/bulk")
    fun sendEmails(@RequestBody request: Set<CertificateEmailRequest>) {
        emailSenderService.sendBulkEmail(request)
    }
}
