package com.shoejs.common.pagination

sealed class PaginationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class PaginationInvalidException(message: String) : PaginationException(message)
