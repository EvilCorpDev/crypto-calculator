package com.androidghost77.cryptocalculator.repos.entities

import com.androidghost77.cryptocalculator.enums.UserRole
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    @field:Id val id: String,
    val username: String,
    val passwordHash: String,
    val email: String,
    val role: UserRole,
)
