package com.shoejs.features.journal

import com.shoejs.features.tag.Tag
import com.shoejs.features.tag.TagResponse
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

data class JournalWithTags(
    val journal: Journal, val tags: Set<Tag>
)

@Serializable
data class JournalRequest(
    val title: String,
    val content: String,
    val tagIds: Set<Long>,
)

@Serializable
data class JournalResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val createdAt: String,
    val updatedAt: String?,
    val updateCount: Long,
    val tags: Set<TagResponse>,
)

fun Journal.toJournalResponse(username: String, tags: Set<TagResponse>) = JournalResponse(
    id = this.id,
    title = this.title,
    content = this.content,
    author = username,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt?.toString(),
    updateCount = this.updateCount,
    tags = tags,
)

@Serializable
data class JournalSummaryResponse(
    val id: Long, val title: String, val author: String, val createdAt: String, val updatedAt: String?
)

fun Journal.toJournalSummaryResponse(username: String) = JournalSummaryResponse(
    id = this.id,
    title = this.title,
    author = username,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt?.toString()
)
