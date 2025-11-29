package com.shoejs.features.user

import com.shoejs.database.tables.Users
import com.shoejs.database.tables.toUser
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDate
import java.time.LocalDateTime

object UserRepository {

    fun createUser(
        firstName: String,
        lastName: String,
        dateOfBirth: LocalDate,
        username: String,
        email: String,
        password: String
    ): User? = transaction {
        addLogger(StdOutSqlLogger)
        Users.insert {
            it[Users.firstName] = firstName
            it[Users.lastName] = lastName
            it[Users.dateOfBirth] = dateOfBirth
            it[Users.username] = username
            it[Users.email] = email
            it[Users.password] = password
        }.resultedValues?.singleOrNull()?.toUser()
    }

    fun getUserByUsername(username: String): User? = transaction {
        addLogger(StdOutSqlLogger)
        Users.selectAll().where { Users.username eq username }.firstOrNull()?.toUser()
    }

    fun getUserById(id: Long): User? = transaction {
        addLogger(StdOutSqlLogger)
        Users.selectAll().where { Users.id eq id }.firstOrNull()?.toUser()
    }

    fun updateUserById(
        id: Long,
        username: String?,
        email: String?,
        gender: Gender?,
        phoneNumber: String?
    ): User? = transaction {
        addLogger(StdOutSqlLogger)
        var updated = false
        Users.update({ Users.id eq id }) { userRow ->
            username?.let {
                userRow[Users.username] = it
                updated = true
            }
            email?.let {
                userRow[Users.email] = it
                updated = true
            }
            gender?.let {
                userRow[Users.gender] = it
                updated = true
            }
            phoneNumber?.let {
                userRow[Users.phoneNumber] = it
                updated = true
            }
            if (updated) {
                userRow[Users.updatedAt] = LocalDateTime.now()
            }
        }
        getUserById(id)
    }
}
