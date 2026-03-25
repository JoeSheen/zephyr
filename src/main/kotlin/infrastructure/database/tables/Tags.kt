package com.shoejs.infrastructure.database.tables

import com.shoejs.features.tag.Tag
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

object Tags : LongIdTable() {
    val name = varchar(name = "name", length = 50)
    val color = varchar(name = "color", length = 7) // Hex color, e.g., "#2596BE"
    val isPublic = bool(name = "is_public").default(false)
    val userId = reference(name = "user_id", refColumn = Users.id)
}

fun ResultRow.toTag() = Tag(
    id = this[Tags.id].value,
    name = this[Tags.name],
    color = this[Tags.color],
)
