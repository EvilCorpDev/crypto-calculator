package com.androidghost77.cryptocalculator.repos

import com.androidghost77.cryptocalculator.repos.entities.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {

    fun findByUsername(username: String): User?

    fun deleteByUsername(username: String)
}