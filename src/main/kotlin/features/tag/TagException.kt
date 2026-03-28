package com.shoejs.features.tag

import com.shoejs.common.exception.NotAccessibleException
import com.shoejs.common.exception.ResourceNotFoundException

class TagResourceNotFoundException(message: String, cause: Throwable? = null) :
    ResourceNotFoundException(message, cause)

class TagNotAccessibleException(message: String, cause: Throwable? = null) : NotAccessibleException(message, cause)
