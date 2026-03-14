package com.shoejs.features.journal

sealed class JournalException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class JournalResourceNotFoundException(message: String, cause: Throwable? = null) : JournalException(message, cause)

class JournalNotAccessibleException(message: String, cause: Throwable? = null) : JournalException(message, cause)
