package com.shoejs.features.tag

import com.shoejs.common.query.QueryParams
import com.shoejs.infrastructure.database.tables.Tags
import com.shoejs.infrastructure.database.tables.toTag
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object TagRepository {

    fun saveTag(userId: Long, tagRequest: TagRequest): Tag = transaction {
        addLogger(StdOutSqlLogger)

        val savedId = Tags.insertAndGetId {
            it[Tags.name] = tagRequest.name
            it[Tags.color] = tagRequest.hexColor
            it[Tags.isPublic] = tagRequest.isPublic
            it[Tags.userId] = userId
        }.value

        Tags.selectAll().where { (Tags.id eq savedId) }.first().toTag()
    }

    fun getTagById(userId: Long, tagId: Long): Tag? = transaction {
        addLogger(StdOutSqlLogger)

        Tags.selectAll().where { (Tags.id eq tagId) and ((Tags.userId eq userId) or Tags.isPublic) }
            .map { it.toTag() }.singleOrNull() ?: throw RuntimeException("")
    }

    fun getAllTags(queryParams: QueryParams): List<Tag> = transaction {
        addLogger(StdOutSqlLogger)
        val orderByQuery = buildOrderByQuery(queryParams.orderField, queryParams.ascending)

        val offset = ((queryParams.page - 1) * queryParams.size).toLong()
        Tags.selectAll().offset(offset).limit(queryParams.size).orderBy(orderByQuery).map { it.toTag() }
    }

    fun countTags(): Long = transaction {
        Tags.selectAll().count()
    }

    fun updateTagById(id: Long, name: String, color: String): Tag? = transaction {
        addLogger(StdOutSqlLogger)
        val row = Tags.update({ Tags.id eq id }) { tagRow ->
            tagRow[Tags.name] = name
            tagRow[Tags.color] = color
        }
        if (row == 0) return@transaction null
        getTagById(id, 0)
    }

    fun deleteTagById(id: Long): Boolean = transaction {
        addLogger(StdOutSqlLogger)
        Tags.deleteWhere { Tags.id eq id } > 0
    }

    private fun buildOrderByQuery(orderField: String?, ascending: Boolean): Pair<Column<out Any?>, SortOrder> {
        return when (orderField) {
            "name" -> if (ascending) Tags.name to SortOrder.ASC else Tags.name to SortOrder.DESC
            "color" -> if (ascending) Tags.color to SortOrder.ASC else Tags.color to SortOrder.DESC
            else -> if (ascending) Tags.id to SortOrder.ASC else Tags.id to SortOrder.DESC
        }
    }

}
