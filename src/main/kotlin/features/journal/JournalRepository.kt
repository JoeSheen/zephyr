package com.shoejs.features.journal

import com.shoejs.common.query.QueryParams
import com.shoejs.infrastructure.database.tables.JournalTags
import com.shoejs.infrastructure.database.tables.Journals
import com.shoejs.infrastructure.database.tables.Tags
import com.shoejs.infrastructure.database.tables.toJournal
import com.shoejs.infrastructure.database.tables.toTag
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.LowerCase
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

object JournalRepository {

    fun createJournal(userId: Long, journalRequest: JournalRequest): JournalWithTags = transaction {
        addLogger(StdOutSqlLogger)

        val savedId = Journals.insertAndGetId {
            it[Journals.title] = journalRequest.title
            it[Journals.content] = journalRequest.content
            it[Journals.authorId] = userId
        }.value

        journalRequest.tagIds.forEach { tagId ->
            JournalTags.insert {
                it[JournalTags.journalId] = savedId
                it[JournalTags.tagId] = tagId
            }
        }

        val journal = Journals.selectAll().where { (Journals.id eq savedId) }.first().toJournal()

        val tags = Tags.join(JournalTags, JoinType.INNER, Tags.id, JournalTags.tagId).selectAll()
            .where { JournalTags.journalId eq savedId }.map { row -> row.toTag() }.toSet()

        JournalWithTags(journal, tags)
    }

    fun getJournalById(userId: Long, journalId: Long): JournalWithTags = transaction {
        addLogger(StdOutSqlLogger)

        val journal: Journal
        try {
            journal = Journals.selectAll().where {
                (Journals.id eq journalId) and (Journals.authorId eq userId)
            }.first().toJournal()
        } catch (e: NoSuchElementException) {
            throw JournalResourceNotFoundException("Journal with [id: $journalId] does not exist", e)
        }

        val tags = Tags.join(JournalTags, JoinType.INNER, Tags.id, JournalTags.tagId).selectAll()
            .where { (JournalTags.journalId eq journal.id) }.map { row -> row.toTag() }.toSet()

        JournalWithTags(journal, tags)
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

    fun updateJournalById(userId: Long, journalId: Long, journalRequest: JournalRequest): JournalWithTags =
        transaction {
            addLogger(StdOutSqlLogger)

            val rowsUpdated = Journals.update(
                where = { (Journals.id eq journalId) and (Journals.authorId eq userId) }) { journalRow ->
                journalRow[Journals.title] = journalRequest.title
                journalRow[Journals.content] = journalRequest.content
                journalRow[Journals.updatedAt] = LocalDateTime.now()
                with(receiver = SqlExpressionBuilder) {
                    journalRow.update(column = Journals.updateCount, value = Journals.updateCount + 1)
                }
            }

            if (rowsUpdated == 0) {
                throw JournalNotAccessibleException("Journal $journalId not found or you are not the author")
            }

            // Get current tag IDs
            val currentTagIds = JournalTags.select(JournalTags.tagId).where { JournalTags.journalId eq journalId }
                .map { row -> row[JournalTags.tagId].value }

            // Calculate which to remove and which to add
            val tagsToRemove = currentTagIds.minus(journalRequest.tagIds)
            val tagsToAdd = journalRequest.tagIds.minus(currentTagIds)

            // Delete old tags
            if (tagsToRemove.isNotEmpty()) {
                JournalTags.deleteWhere { (JournalTags.journalId eq journalId) and (JournalTags.tagId inList tagsToRemove) }
            }

            // Insert new tags
            tagsToAdd.forEach { tagId ->
                JournalTags.insert {
                    it[JournalTags.journalId] = journalId
                    it[JournalTags.tagId] = tagId
                }
            }
            getJournalById(userId, journalId)
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
