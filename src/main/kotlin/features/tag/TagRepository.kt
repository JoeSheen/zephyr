package com.shoejs.features.tag

import com.shoejs.database.tables.Tags
import com.shoejs.database.tables.toTag
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object TagRepository {

    fun saveTag(name: String, color: String): Tag? = transaction {
        addLogger(StdOutSqlLogger)
        Tags.insert {
            it[Tags.name] = name
            it[Tags.color] = color
        }.resultedValues?.singleOrNull()?.toTag()
    }
}