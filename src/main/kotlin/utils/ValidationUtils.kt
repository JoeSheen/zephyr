package com.shoejs.utils

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

private val HEX_REGEX = Regex("^#([A-Fa-f0-9]{6})$")

fun isInvalidEmail(email: String?): Boolean {
    if (email.isNullOrBlank()) return true
    return !email.matches(EMAIL_REGEX)
}

fun isHexColor(color: String?): Boolean =
    color?.matches(HEX_REGEX) == true
