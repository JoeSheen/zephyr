package com.shoejs.features.journal

import com.shoejs.common.exception.ResourceNotFoundException

sealed class JournalException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class JournalResourceNotFoundException(message: String, cause: Throwable? = null) : ResourceNotFoundException(message, cause)

class JournalNotAccessibleException(message: String, cause: Throwable? = null) : JournalException(message, cause)
