package com.pp.ppbacked.rest

data class CertificateDto(
    val checksum: String,
    val recipientName: String,
    val recipientSurname: String,
    val recipientEmail: String,
    val daysValid: String,
    val certName: String,
    val issuer: String
)