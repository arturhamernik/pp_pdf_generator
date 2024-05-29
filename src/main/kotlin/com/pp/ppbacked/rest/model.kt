package com.pp.ppbacked.rest

import java.time.LocalDate

data class CertificateResponse(
    val checksum: String,
    val certificateName: String,
    val recipientName: String,
    val recipientLastname: String,
    val recipientEmail: String,
    val daysValid: String,
    val issuer: String
)

data class CertificateRequest(
    val certificateName: String,
    val expirationDate: LocalDate,
    val recipientName: String,
    val recipientLastname: String,
    val recipientEmail: String,
    val issuer: String
)

data class CertificateEmailRequest(
    val checksum: String,
    val recipientName: String,
    val recipientLastname: String,
    val recipientEmail: String,
    val issuer: String
)