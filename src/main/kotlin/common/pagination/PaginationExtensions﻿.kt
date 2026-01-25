package com.shoejs.common.pagination

inline fun requirePagination(condition: Boolean, lazyMessage: () -> String) {
    if (!condition) throw PaginationInvalidException(lazyMessage())
}
