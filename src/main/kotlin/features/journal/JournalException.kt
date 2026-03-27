package com.shoejs.features.journal

import com.shoejs.common.exception.NotAccessibleException
import com.shoejs.common.exception.ResourceNotFoundException

class JournalResourceNotFoundException(message: String, cause: Throwable? = null) :
    ResourceNotFoundException(message, cause)

class JournalNotAccessibleException(message: String, cause: Throwable? = null) : NotAccessibleException(message, cause)
