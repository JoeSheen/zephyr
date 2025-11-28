package com.shoejs.database.tables

import com.shoejs.features.user.User
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object Users : LongIdTable() {
    val firstName = varchar(name = "first_name", length = 255)
    val lastName = varchar(name = "last_name", length = 255)
    val dateOfBirth = date("date_of_birth")
    val username = varchar(name = "username", length = 255).uniqueIndex()
    val email = varchar(name = "email", length = 255).uniqueIndex()
    val phoneNumber = varchar(name = "phone_number", length = 255).nullable().uniqueIndex()
    val password = varchar(name = "password", length = 255)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").nullable()
}

fun ResultRow.toUser() = User(
    id = this[Users.id].value,
    firstName = this[Users.firstName],
    lastName = this[Users.lastName],
    dateOfBirth = this[Users.dateOfBirth],
    username = this[Users.username],
    email = this[Users.email],
    phoneNumber = this[Users.phoneNumber],
    password = this[Users.password],
    createdAt = this[Users.createdAt],
    updatedAt = this[Users.updatedAt]
)
