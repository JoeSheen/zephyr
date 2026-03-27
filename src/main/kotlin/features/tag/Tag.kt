package com.shoejs.features.tag

import kotlinx.serialization.Serializable

data class Tag(
    val id: Long,
    val name: String,
    val color: String,
    val isPublic: Boolean,
    val userId: Long,
)

@Serializable
data class TagRequest(
    val name: String,
    val hexColor: String,
    val isPublic: Boolean = false,
)

@Serializable
data class TagResponse(
    val id: Long,
    val name: String,
    val hexColor: String,
    val isPublic: Boolean,
)

fun Tag.toTagResponse() = TagResponse(
    id = this.id,
    name = this.name,
    hexColor = this.color,
    isPublic = this.isPublic,
)
