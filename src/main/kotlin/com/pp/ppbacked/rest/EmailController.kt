package com.pp.ppbacked.rest

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/emails")
class EmailController {

    @PostMapping
    fun send(@RequestPart file: MultipartFile, @RequestPart issuer: String) {
    }
}
