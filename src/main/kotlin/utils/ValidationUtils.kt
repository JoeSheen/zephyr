package com.shoejs.utils

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

private val HEX_REGEX = Regex("^#([A-Fa-f0-9]{6})$")

private val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

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

fun isValidPhoneNumber(numberToParse: String, defaultRegion: String? = null): Pair<Boolean, String?> {
    val region = defaultRegion ?: "GB"
    return try {
        val phoneNumber = phoneUtil.parse(numberToParse, region)
        if (phoneUtil.isValidNumberForRegion(phoneNumber, region)) {
            true to null
        } else {
            false to "Invalid phone number for region '$region'"
        }
    } catch (e: NumberParseException) {
        false to "Failed to parse phone number: ${e.message}"
    }
}

fun formatPhoneNumber(numberToParse: String?, defaultRegion: String? = null): String? {
    val region = defaultRegion ?: "GB"
    return try {
        val phoneNumber = phoneUtil.parse(numberToParse, region)
        if (phoneUtil.isValidNumber(phoneNumber)) {
            phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
        } else null
    } catch (_: NumberParseException) {
        null
    }
}
