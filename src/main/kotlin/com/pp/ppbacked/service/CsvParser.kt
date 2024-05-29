package com.pp.ppbacked.service

import com.pp.ppbacked.service.CsvHeader.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class CsvParser {

    fun readCsvFile(file: MultipartFile): List<CsvCertificateRecord> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return file.inputStream.bufferedReader().use { reader ->
            CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()).use { csvParser ->
                csvParser.records.map {
                    CsvCertificateRecord(
                        it.get(CERTIFICATE_NAME),
                        it.get(FIRST_NAME),
                        it.get(LAST_NAME),
                        it.get(EMAIL),
                        LocalDate.parse(it.get(EXPIRATION_DATE), formatter)
                    )
                }
            }
        }
    }
}