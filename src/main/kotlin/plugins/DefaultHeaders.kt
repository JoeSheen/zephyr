package com.shoejs.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.defaultheaders.DefaultHeaders

fun Application.configureDefaultHeaders() {
    install(DefaultHeaders) {
        header("Content-Security-Policy", "frame-ancestors 'none'")
        header("Referrer-Policy", "no-referrer")
        header("Zephyr-Version", "2026-01::0.0.1")
        header("Zephyr-Build-Id", "019b741d")
    }
}
