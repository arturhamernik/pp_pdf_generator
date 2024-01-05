package com.pp.ppbacked.service

import com.pp.ppbacked.service.CsvHeader.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class CsvParser(private val certificateValidator: CertificateValidator) {

    fun readCsvFile(file: MultipartFile): List<CsvCertificateRecord> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return file.inputStream.bufferedReader().use { reader ->
            CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()).use { csvParser ->
                csvParser.records.mapIndexed { index, csvRecord ->
                    val lineNumber = index + 1
                    val csvCertificateRecord = CsvCertificateRecord(
                        csvRecord.get(CERTIFICATE_NAME),
                        csvRecord.get(FIRST_NAME),
                        csvRecord.get(LAST_NAME),
                        csvRecord.get(EMAIL),
                        LocalDate.parse(csvRecord.get(EXPIRATION_DATE), formatter)
                    )

                    certificateValidator.validateCertificate(lineNumber, csvCertificateRecord)
                    csvCertificateRecord
                }
            }
        }
    }
}