package com.shoejs.common.query

import com.shoejs.common.pagination.requirePagination
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall

private fun Parameters.int(name: String, default: Int) = this[name]?.toIntOrNull() ?: default

private fun Parameters.bool(name: String) = this[name]?.toBooleanStrictOrNull() ?: true

private fun Parameters.string(name: String) = this[name]

data class QueryParams(
    val page: Int,
    val size: Int,
    val query: String? = null,
    val orderField: String? = null,
    val ascending: Boolean = true
)

fun ApplicationCall.getQueryParameters(defaultPage: Int, defaultSize: Int): QueryParams {
    val page = parameters.int("page", defaultPage)
    val size = parameters.int("size", defaultSize)
    val query = parameters.string("query")
    val orderField = parameters.string("orderField")
    val ascending = parameters.bool("ascending")

    requirePagination(page > 0) {
        "Parameter 'page: ${page}' must be greater than or equal to 1"
    }

    requirePagination(size in 1..1000) {
        "Parameter 'size: ${size}' must be between 1 and 1000"
    }

    return QueryParams(page, size, query, orderField, ascending)
}
