package com.pp.ppbacked

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class CsvParser {
    private val logger = LoggerFactory.getLogger(CsvParser::class.java)

    fun readCsvFile(file: MultipartFile): List<PersonCertificate> {
        val certificates = mutableListOf<PersonCertificate>()
        file.inputStream.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()).use { csvParser ->
                    for (csvRecord in csvParser) {
                        val certName = csvRecord.get("CertName")
                        val firstName = csvRecord.get("FirstName")
                        val lastName = csvRecord.get("LastName")
                        val email = csvRecord.get("Email")
                        val expirationDate = csvRecord.get("ExpirationDate")
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val expirationDateAsLocalDate = LocalDate.parse(expirationDate, formatter)
                        val daysValid = expirationDateAsLocalDate.toEpochDay() - LocalDate.now().toEpochDay()

                        if (certName != "" &&
                            firstName != "" &&
                            lastName != "" &&
                            email != "" &&
                            daysValid > 0
                        ) {
                            certificates.add(PersonCertificate(certName, firstName, lastName, email, daysValid.toString(), expirationDateAsLocalDate))
                        } else {
                            logger.info("Validation error")
                        }
                    }
                }
            }
        }
        return certificates
    }
}