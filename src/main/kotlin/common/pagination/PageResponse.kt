package com.shoejs.common.pagination

import kotlinx.serialization.Serializable

@Serializable
data class PageResponse<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val totalItems: Long,
    val totalPages: Int
)
