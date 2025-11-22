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

@Serializable
data class TagResponse(
    val id: Long,
    val name: String,
    val hexColor: String
)

fun Tag.toTagResponse() = TagResponse(
    id = this.id,
    name = this.name,
    hexColor = this.color
)
