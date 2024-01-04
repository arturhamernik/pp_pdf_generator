package com.pp.ppbacked.dto

data class CertificateDto(
    val checksum: String,
    val recipientName: String,
    val recipientSurname: String,
    val recipientEmail: String,
    val daysValid: String,
    val certName: String,
    val issuer: String
)
