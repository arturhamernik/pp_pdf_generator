package com.pp.ppbacked.common

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {

    @Bean
    fun openAPI(
        @Value("\${info.app.name}") appName: String?,
        @Value("\${info.app.version}") appVersion: String?,
        @Value("\${server.servlet.context-path}") servicePath: String
    ): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("$appName API")
                    .description("Description")
                    .version(appVersion)
            )
    }
}
