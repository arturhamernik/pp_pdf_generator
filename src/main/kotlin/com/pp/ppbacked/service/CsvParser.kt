package com.pp.ppbacked.service

import com.pp.ppbacked.dto.CsvCertificateRecord
import com.pp.ppbacked.dto.CsvHeader.CERTIFICATE_NAME
import com.pp.ppbacked.dto.CsvHeader.FIRST_NAME
import com.pp.ppbacked.dto.CsvHeader.LAST_NAME
import com.pp.ppbacked.dto.CsvHeader.EMAIL
import com.pp.ppbacked.dto.CsvHeader.EXPIRATION_DATE
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class CsvParser(private val certificateValidator: CertificateValidator) {

    fun readCsvFile(file: MultipartFile): List<CsvCertificateRecord> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var lineNumber = 0
        val certificates = mutableListOf<CsvCertificateRecord>()
        file.inputStream.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()).use { csvParser ->
                    for (csvRecord in csvParser) {
                        lineNumber++
                        val csvCertificateRecord = CsvCertificateRecord(
                            csvRecord.get(CERTIFICATE_NAME),
                            csvRecord.get(FIRST_NAME),
                            csvRecord.get(LAST_NAME),
                            csvRecord.get(EMAIL),
                            LocalDate.parse(csvRecord.get(EXPIRATION_DATE), formatter)
                        )

                        certificateValidator.validateCertificate(lineNumber, csvCertificateRecord)
                        certificates.add(csvCertificateRecord)
                    }
                }
            }
        }

        return certificates
    }
}
