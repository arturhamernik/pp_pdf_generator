package com.pp.ppbacked.rest

import com.fasterxml.jackson.annotation.JsonProperty

data class CertificateDto(
    val checksum: String,
    val recipientName: String,
    val recipientSurname: String,
    val recipientEmail: String,
    val daysValid: String,
    val certName: String,
    val issuer: String
)

data class CertificateEmailRequest(
    @JsonProperty("checksum") val checksum: String,
    @JsonProperty("recipientName") val recipientName: String,
    @JsonProperty("recipientSurname") val recipientSurname: String,
    @JsonProperty("recipientEmail") val recipientEmail: String,
    @JsonProperty("issuer") val issuer: String
)
