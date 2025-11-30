package com.shoejs.features.journal

import com.shoejs.database.tables.Journals
import com.shoejs.database.tables.toJournal
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
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
}
