package com.shoejs.features.tag

import com.shoejs.database.tables.Tags
import com.shoejs.database.tables.toTag
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object TagRepository {

    fun saveTag(name: String, color: String): Tag? = transaction {
        addLogger(StdOutSqlLogger)
        Tags.insert {
            it[Tags.name] = name
            it[Tags.color] = color
        }.resultedValues?.singleOrNull()?.toTag()
    }

    fun getTagById(id: Long): Tag? = transaction {
        addLogger(StdOutSqlLogger)
        Tags.selectAll().where { Tags.id eq id }.firstOrNull()?.toTag()
    }

    fun getAllTags(): List<Tag> = transaction {
        addLogger(StdOutSqlLogger)
        Tags.selectAll().map { it.toTag() }
    }

    fun updateTagById(id: Long, name: String, color: String): Tag? = transaction {
        addLogger(StdOutSqlLogger)
        val row = Tags.update({ Tags.id eq id }) { tagRow ->
            tagRow[Tags.name] = name
            tagRow[Tags.color] = color
        }
        if (row == 0) return@transaction null
        getTagById(id)
    }

    fun deleteTagById(id: Long): Boolean = transaction {
        addLogger(StdOutSqlLogger)
        Tags.deleteWhere { Tags.id eq id } > 0
    }

}
