package com.pp.ppbacked.service

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.PageSizeUnits
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSDocument
import org.apache.pdfbox.cos.COSString
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.jsoup.Jsoup
import org.jsoup.helper.W3CDom
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream

@Service
class CertificatePdfGenerator {
    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private val htmlTemplate: String by lazy {
            this::class.java.getResourceAsStream("/template/certificate_template.html")
                ?.bufferedReader()
                ?.use { it.readText() }
                ?: throw IllegalArgumentException("Template file not found")
        }
    }

    fun generatePdfBytes(csvCertificateRecord: CsvCertificateRecord, issuer: String): ByteArray {
        val replacements = mapOf(
            "\${firstName}" to csvCertificateRecord.firstName,
            "\${lastName}" to csvCertificateRecord.lastName,
            "\${certName}" to csvCertificateRecord.certName,
            "\${expirationDate}" to csvCertificateRecord.expirationDate.toString(),
            "\${issuer}" to issuer
        )

        val htmlContent = replaceTemplateValues(htmlTemplate, replacements)
        return generatePdfFromHtml(htmlContent)
    }

    private fun generatePdfFromHtml(htmlContent: String): ByteArray {
        val document = Jsoup.parse(htmlContent).apply {
            outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml)
        }

        val w3cDoc = W3CDom().fromJsoup(document)
        val pdfBytes = renderPdf(w3cDoc)
        return cleanPdf(pdfBytes)
    }

    private fun renderPdf(w3cDoc: org.w3c.dom.Document): ByteArray {
        return ByteArrayOutputStream().use { outputStream ->
            PdfRendererBuilder().apply {
                toStream(outputStream)
                withW3cDocument(w3cDoc, "/")
                useDefaultPageSize(
                    PDRectangle.A4.height / 72.0f,
                    PDRectangle.A4.width / 72.0f,
                    PageSizeUnits.INCHES
                )
                run()
            }
            outputStream.toByteArray()
        }
    }

    private fun cleanPdf(pdfBytes: ByteArray): ByteArray {
        return ByteArrayOutputStream().use { outputStream ->
            PDDocument.load(pdfBytes).use { pdDocument ->
                hardcodeDocumentID(pdDocument.document)
                pdDocument.save(outputStream)
            }
            outputStream.toByteArray()
        }
    }

    private fun hardcodeDocumentID(document: COSDocument) {
        val cosArray: COSArray = document.documentID
        try {
            if (cosArray.size() > 1) {
                cosArray[0] = COSString("X")
                cosArray[1] = COSString("X")
                document.documentID = cosArray
            } else {
                logger.warn("COSArray does not have enough elements")
            }
        } catch (e: Exception) {
            logger.error("An error occurred while updating the document ID", e)
        }
    }

    private fun replaceTemplateValues(template: String, replacements: Map<String, String>): String {
        val stringBuilder = StringBuilder(template)
        for ((key, value) in replacements) {
            var index = stringBuilder.indexOf(key)
            while (index != -1) {
                stringBuilder.replace(index, index + key.length, value)
                index = stringBuilder.indexOf(key, index + value.length)
            }
        }
        return stringBuilder.toString()
    }
}
