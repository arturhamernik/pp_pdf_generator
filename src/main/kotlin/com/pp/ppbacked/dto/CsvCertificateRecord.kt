package com.pp.ppbacked.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class CsvCertificateRecord(
    @field:JsonProperty("CERTIFICATE_NAME") val certName: String,
    @field:JsonProperty("FIRST_NAME") val firstName: String,
    @field:JsonProperty("LAST_NAME") val lastName: String,
    @field:JsonProperty("EMAIL") val email: String,
    @field:JsonProperty("EXPIRATION_DATE") val expirationDate: LocalDate
)
