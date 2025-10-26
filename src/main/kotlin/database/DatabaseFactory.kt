package com.shoejs.database

import com.shoejs.database.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object DatabaseFactory {

    private val logger = LoggerFactory.getLogger(DatabaseFactory::class.java)

    fun init(url: String, driver: String, user: String, password: String) {

        Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )

        // Create tables if they don't exist
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Journals)
        }

        logger.info("Connected to postgres database at $url")
    }
}
