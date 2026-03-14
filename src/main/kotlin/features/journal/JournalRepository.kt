package com.shoejs.features.journal

import com.shoejs.common.query.QueryParams
import com.shoejs.infrastructure.database.tables.Journals
import com.shoejs.infrastructure.database.tables.toJournal
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.LowerCase
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

object JournalRepository {

    fun createJournal(title: String, content: String, userId: Long): Journal = transaction {
        addLogger(StdOutSqlLogger)

        val savedId = Journals.insertAndGetId {
            it[Journals.title] = title
            it[Journals.content] = content
            it[Journals.authorId] = userId
        }.value

        Journals.selectAll().where { Journals.id eq savedId }.first().toJournal()
    }

    fun getJournalById(userId: Long, journalId: Long): Journal = transaction {
        addLogger(StdOutSqlLogger)

        try {
            Journals.selectAll().where {
                (Journals.id eq journalId) and (Journals.authorId eq userId)
            }.first().toJournal()
        } catch (e: NoSuchElementException) {
            throw JournalResourceNotFoundException("Journal with [id: $journalId] does not exist", e)
        }
    }

    fun getAllJournals(userId: Long, queryParams: QueryParams): List<Journal> = transaction {
        addLogger(StdOutSqlLogger)

        val conditions = mutableListOf(Journals.authorId eq userId)

        if (!queryParams.query.isNullOrBlank()) {
            conditions += (LowerCase(Journals.title) like "%${queryParams.query.lowercase()}%")
        }

        val finalCondition = conditions.reduce { acc, op -> acc and op }

        val orderByQuery = buildOrderByQuery(queryParams.orderField, queryParams.ascending)

        val offset = ((queryParams.page - 1) * queryParams.size).toLong()

        Journals.selectAll().where { finalCondition }.offset(start = offset).limit(count = queryParams.size)
            .orderBy(orderByQuery).map { resultRow -> resultRow.toJournal() }
    }

    fun countJournals(userId: Long, query: String?): Long = transaction {
        addLogger(StdOutSqlLogger)
        var condition: Op<Boolean> = Journals.authorId eq userId

        if (!query.isNullOrBlank()) {
            condition = condition and (LowerCase(Journals.title) like "%${query.lowercase()}%")
        }

        Journals.selectAll().where { condition }.count()
    }

    fun updateJournalById(id: Long, title: String, content: String): Journal? = transaction {
        addLogger(StdOutSqlLogger)
        Journals.update(where = { Journals.id eq id }) { journalRow ->
            with(receiver = SqlExpressionBuilder) {
                journalRow[Journals.title] = title
                journalRow[Journals.content] = content
                journalRow[Journals.updatedAt] = LocalDateTime.now()
                journalRow.update(column = Journals.updateCount, value = Journals.updateCount + 1)
            }
        }
        getJournalById(id, 7)
    }

    fun deleteJournalById(userId: Long, journalId: Long): Boolean = transaction {
        addLogger(StdOutSqlLogger)
        Journals.deleteWhere { (Journals.id eq journalId) and (Journals.authorId eq userId) } > 0
    }

    private fun buildOrderByQuery(orderField: String?, ascending: Boolean): Pair<Column<out Any?>, SortOrder> {
        return when (orderField) {
            "title" -> if (ascending) Journals.title to SortOrder.ASC else Journals.title to SortOrder.DESC
            "createdAt" -> if (ascending) Journals.createdAt to SortOrder.ASC else Journals.createdAt to SortOrder.DESC
            "updatedAt" -> if (ascending) Journals.updatedAt to SortOrder.ASC else Journals.updatedAt to SortOrder.DESC
            else -> if (ascending) Journals.id to SortOrder.ASC else Journals.id to SortOrder.DESC
        }
    }
}
