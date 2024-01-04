package com.pp.ppbacked.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/email")
class EmailController() {

    @PostMapping()
    fun send(@RequestPart file: MultipartFile, @RequestPart issuer: String) {
    }
}
