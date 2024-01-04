package com.pp.ppbacked.service

import com.pp.ppbacked.common.ValidationException
import com.pp.ppbacked.dto.CsvCertificateRecord
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CertificateValidator {
     fun validateCertificate(index: Int, certificate: CsvCertificateRecord) {
        if (certificate.certName.isEmpty()) {
            throw ValidationException("Certificate name empty at line $index")
        } else if (certificate.firstName.isEmpty()) {
            throw ValidationException("Certificate recipient name empty at line $index")
        }
        else if (certificate.lastName.isEmpty()) {
            throw ValidationException("Certificate recipient surname empty at line $index")
        }
        else if(certificate.email.isEmpty()) {
            throw ValidationException("Certificate recipient email empty at line $index")
        }
        else if(certificate.expirationDate.isBefore(LocalDate.now())) {
            throw ValidationException("Certificate expiration date is in the past at line $index")
        }
    }
}
