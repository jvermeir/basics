package nl.vermeir.kotlin.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import nl.vermeir.kotlin.repository.CustomerAlreadyExistsException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomerAlreadyExistsException::class)
    fun handleCustomerAlreadyExistsException(ex: CustomerAlreadyExistsException): ResponseEntity<String?> {
        return ResponseEntity<String?>(ex.message, HttpStatus.CONFLICT)
    }
}