package com.shoejs.database.tables

import com.shoejs.features.journal.Journal
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Journals : LongIdTable() {
    val title = varchar(name = "title", length = 255)
    val content = text(name = "content")
    //val author = reference(name = "author_id", foreign = Users)
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime(name = "updated_at").nullable()
    val updateCount = long(name = "update_count").default(0)
}

fun ResultRow.toJournal() = Journal(
    id = this[Journals.id].value,
    title = this[Journals.title],
    content = this[Journals.content],
    createdAt = this[Journals.createdAt],
    updatedAt = this[Journals.updatedAt],
    updateCount = this[Journals.updateCount]
)
