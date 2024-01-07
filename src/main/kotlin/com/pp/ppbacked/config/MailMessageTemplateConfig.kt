package com.pp.ppbacked.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage

@Configuration
class MailMessageTemplateConfig {
    @Bean
    fun certificateCreated(): SimpleMailMessage {
        val template = SimpleMailMessage()
        template.subject = "Certificate issued!"
        template.text = """
            Dear %s %s, 
            
            your certificate is available in attachment in this message.
            
            Best regards,
            %s team
        """.trimIndent()

        return template
    }
}
