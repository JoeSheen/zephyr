package com.shoejs.features.auth.refresh

internal fun String.toRefreshTokenPair(): Pair<Long, String> =
    split(":", limit = 2).let { (userId, username) ->
        Pair(userId.toLong(), username)
    }
