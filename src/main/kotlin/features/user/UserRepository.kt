package com.shoejs.features.user

import com.shoejs.database.tables.Users
import com.shoejs.database.tables.toUser
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object UserRepository {

    fun createUser(
        firstName: String,
        lastName: String,
        dateOfBirth: LocalDate,
        username: String,
        email: String,
        password: String
    ): User? {
        return transaction {
            Users.insert {
                it[Users.firstName] = firstName
                it[Users.lastName] = lastName
                it[Users.dateOfBirth] = dateOfBirth
                it[Users.username] = username
                it[Users.email] = email
                it[Users.password] = password
            }.resultedValues?.singleOrNull()?.toUser()
        }
    }

    fun getUserByUsername(username: String): User? {
        return transaction {
            Users.select(Users.username eq username).firstOrNull()?.toUser()
        }
    }
}
