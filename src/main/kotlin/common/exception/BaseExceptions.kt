package com.shoejs.common.exception

open class ResourceNotFoundException(message: String, cause: Throwable? = null): RuntimeException(message, cause)

open class NotAccessibleException(message: String, cause: Throwable? = null): RuntimeException(message, cause)
