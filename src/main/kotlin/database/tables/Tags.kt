package com.shoejs.database.tables

import com.shoejs.features.tag.Tag
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Tags : LongIdTable() {
    val name = varchar(name = "name", length = 50)
    val color = varchar(name = "color", length = 7) // Hex color, e.g., "#2596BE"
}

fun ResultRow.toTag() = Tag(
    id = this[Tags.id].value,
    name = this[Tags.name],
    color = this[Tags.color],
)

// TODO: move to own file?
object JournalTags : Table() {
    val journal = reference(
        "journal_id",
        Journals,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE
    )

    val tag = reference(
        "tag_id",
        Tags,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE
    )

    override val primaryKey = PrimaryKey(journal, tag)
}
