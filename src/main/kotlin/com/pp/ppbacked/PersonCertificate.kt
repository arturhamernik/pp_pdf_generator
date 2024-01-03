package com.pp.ppbacked

import java.time.LocalDate

data class PersonCertificate(
    val certName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val daysValid: String,
    val expirationDate: LocalDate
    )
