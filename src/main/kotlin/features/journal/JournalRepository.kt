package com.shoejs.features.journal

import com.shoejs.database.tables.Journals
import com.shoejs.database.tables.toJournal
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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
}
