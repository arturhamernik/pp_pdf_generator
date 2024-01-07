package com.pp.ppbacked.rest

import com.pp.ppbacked.service.CertificateService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/certificates")
class CertificateController(
    private val certificateService: CertificateService
) {

    @PostMapping
    fun generate(@RequestPart file: MultipartFile, @RequestPart issuer: String): List<CertificateDto> {
        return certificateService.generate(file, issuer)
    }
}
