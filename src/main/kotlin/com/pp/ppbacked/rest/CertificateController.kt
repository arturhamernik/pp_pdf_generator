package com.pp.ppbacked.rest

import com.pp.ppbacked.service.CertificateService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/certificates")
class CertificateController(
    private val certificateService: CertificateService
) {

    @PostMapping
    fun generateAndSaveCert(@RequestBody request: CertificateRequest): CertificateResponse {
        return certificateService.generateAndSaveCert(request)
    }

    @PostMapping("/bulk")
    fun generateAndSaveCerts(@RequestPart file: MultipartFile, @RequestPart issuer: String): List<CertificateResponse> {
        return certificateService.generateAndSaveCertsFromCsv(file, issuer)
    }

}