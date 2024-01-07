package com.pp.ppbacked.common

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleValidationException(ex: ValidationException): ResponseEntity<String> {
        logger.error(ex.message)
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.message)
    }
}
