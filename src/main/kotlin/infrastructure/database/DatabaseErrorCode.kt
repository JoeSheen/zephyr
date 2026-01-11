package com.shoejs.infrastructure.database

enum class DatabaseErrorCode(val code: String) {
    UNIQUE_CONSTRAINT_VIOLATION("23505")
}
