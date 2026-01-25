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
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

object JournalRepository {

    fun createJournal(title: String, content: String): Journal? = transaction {
        addLogger(StdOutSqlLogger)
        Journals.insert {
            it[Journals.title] = title
            it[Journals.content] = content
        }.resultedValues?.singleOrNull()?.toJournal()
    }

    fun getJournalById(id: Long): Journal? = transaction {
        addLogger(StdOutSqlLogger)
        Journals.selectAll().where { Journals.id eq id }.firstOrNull()?.toJournal()
    }

    fun getAllJournals(queryParams: QueryParams): List<Journal> = transaction {
        addLogger(StdOutSqlLogger)
        val conditions = mutableListOf<Op<Boolean>>()
        if (!queryParams.query.isNullOrBlank()) {
            conditions += (LowerCase(Journals.title) like "%${queryParams.query.lowercase()}%")
        }

        // Note for future work:
        // Things like checking author/user can be done like so:
        /*
        if (!author.isNullOrBlank()) {
            conditions += Blogs.author eq author
        }
         */

        val finalCondition = conditions.reduceOrNull { acc, op -> acc and op }

        val orderByQuery = buildOrderByQuery(queryParams.orderField, queryParams.ascending)

        val offset = ((queryParams.page - 1) * queryParams.size).toLong()
        val queryBuilder = if (finalCondition != null) {
            Journals.selectAll().where { finalCondition }.offset(start = offset).limit(count = queryParams.size)
                .orderBy(orderByQuery)
        } else {
            Journals.selectAll().offset(start = offset).limit(count = queryParams.size)
                .orderBy(orderByQuery)
        }

        queryBuilder.map { resultRow -> resultRow.toJournal() }
    }

    fun countJournals(query: String?): Long = transaction {
        addLogger(StdOutSqlLogger)
        Journals.selectAll().apply {
            if (!query.isNullOrBlank()) {
                where { LowerCase(Journals.title) like "%${query.lowercase()}%" }
            }
        }.count()
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
        getJournalById(id)
    }

    fun deleteJournalById(id: Long): Boolean = transaction {
        addLogger(StdOutSqlLogger)
        Journals.deleteWhere { Journals.id eq id } > 0
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
