package com.shoejs.features.tag

import kotlinx.serialization.Serializable

data class Tag(
    val id: Long,
    val name: String,
    val color: String
)

@Serializable
data class TagRequest(
    val name: String,
    val hexColor: String
)
