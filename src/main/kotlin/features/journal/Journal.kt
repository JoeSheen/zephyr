package com.shoejs.features.journal

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class Journal(
    val id: Long,
    val title: String,
    val content: String,
    val authorId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val updateCount: Long
)

@Serializable
data class JournalRequest(
    val title: String,
    val content: String
)

@Serializable
data class JournalResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val createdAt: String,
    val updatedAt: String?,
    val updateCount: Long
)

fun Journal.toJournalResponse(username: String) = JournalResponse(
    id = this.id,
    title = this.title,
    content = this.content,
    author = username,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt?.toString(),
    updateCount = this.updateCount
)

@Serializable
data class JournalSummaryResponse(
    val id: Long,
    val title: String,
    val author: String,
    val createdAt: String,
    val updatedAt: String?
)

fun Journal.toJournalSummaryResponse(username: String) = JournalSummaryResponse(
    id = this.id,
    title = this.title,
    author = username,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt?.toString()
)
