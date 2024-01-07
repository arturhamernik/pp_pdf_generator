package com.pp.ppbacked.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream

@Service
class CertificatePdfGenerator {

    companion object {
        private val htmlTemplate: String by lazy {
            this::class.java.getResourceAsStream("/template/certificate_template.html")
                ?.bufferedReader()
                ?.use { it.readText() }
                ?: throw IllegalArgumentException("Template file not found")
        }
    }

    fun generatePdfBytes(csvCertificateRecord: CsvCertificateRecord, issuer: String): ByteArray {
        return htmlTemplate
            .replaceValues(
                mapOf(
                    "\${firstName}" to csvCertificateRecord.firstName,
                    "\${lastName}" to csvCertificateRecord.lastName,
                    "\${certName}" to csvCertificateRecord.certName,
                    "\${expirationDate}" to csvCertificateRecord.expirationDate.toString(),
                    "\${issuer}" to issuer
                )
            )
            .let { generatePdfFromHtml(it) }
    }

    private fun generatePdfFromHtml(htmlContent: String): ByteArray {
        val document = Jsoup.parse(htmlContent).apply {
            outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml)
        }

        val renderer = ITextRenderer().apply {
            setDocumentFromString(document.html())
            layout()
        }

        return ByteArrayOutputStream().apply {
            renderer.createPDF(this)
        }.toByteArray()
    }

    private fun String.replaceValues(replacements: Map<String, String>): String {
        return replacements.entries.fold(this) { acc, replacement ->
            acc.replace(replacement.key, replacement.value)
        }
    }
}
