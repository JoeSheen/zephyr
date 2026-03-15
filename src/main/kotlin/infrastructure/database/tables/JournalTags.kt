package com.shoejs.infrastructure.database.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object JournalTags : Table() {
    val journalId = reference(
        name = "journal_id",
        refColumn = Journals.id,
        onDelete = ReferenceOption.CASCADE,
    )

    val tagId = reference(
        name = "tag_id",
        refColumn = Tags.id,
        onDelete = ReferenceOption.CASCADE,
    )

    override val primaryKey = PrimaryKey(journalId, tagId)
}
