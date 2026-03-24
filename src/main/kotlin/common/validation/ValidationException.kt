package com.shoejs.common.validation

sealed class ValidationException(message: String, cause: Throwable? = null): RuntimeException(message, cause)

class InvalidPhoneNumberException(message: String, cause: Throwable? = null) : ValidationException(message, cause)
