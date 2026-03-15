package com.shoejs.infrastructure.database

import com.shoejs.config.DatabaseConfig
import com.shoejs.infrastructure.database.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object DatabaseInitializer {

    private val logger = LoggerFactory.getLogger(DatabaseInitializer::class.java)

    fun init(config: DatabaseConfig) {

        Database.connect(
            url = config.url,
            driver = config.driver,
            user = config.user,
            password = config.password
        )

        // Create tables if they don't exist
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Tags)
            SchemaUtils.create(Journals)
            SchemaUtils.create(JournalTags)
        }

        logger.info("Connected to postgres database at ${config.url}")
    }
}
