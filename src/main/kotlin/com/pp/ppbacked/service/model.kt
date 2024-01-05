package com.pp.ppbacked.service

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

enum class CsvHeader {
    CERTIFICATE_NAME, FIRST_NAME, LAST_NAME, EMAIL, EXPIRATION_DATE
}

data class CsvCertificateRecord(
    @field:JsonProperty("CERTIFICATE_NAME")
    val certName: String,
    @field:JsonProperty("FIRST_NAME")
    val firstName: String,
    @field:JsonProperty("LAST_NAME")
    val lastName: String,
    @field:JsonProperty("EMAIL")
    val email: String,
    @field:JsonProperty("EXPIRATION_DATE")
    val expirationDate: LocalDate
)
