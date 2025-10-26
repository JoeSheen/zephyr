package com.shoejs.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Journals : LongIdTable() {
    val title = varchar(name = "title", length = 255)
    val content = text(name = "content")
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime)
}
