package com.shoejs.utils

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

private val HEX_REGEX = Regex("^#([A-Fa-f0-9]{6})$")

fun isValidEmail(email: String): Pair<Boolean, String?> {
    if (email.isBlank()) return false to "Email cannot be blank"
    return if (!email.matches(EMAIL_REGEX)) false to "Invalid email format"
    else true to null
}

fun isValidHexColor(color: String): Pair<Boolean, String?> {
    if (color.isBlank()) return false to "Hex color cannot be blank"
    return if (!color.matches(HEX_REGEX)) false to "Invalid hex color format"
    else true to null
}
