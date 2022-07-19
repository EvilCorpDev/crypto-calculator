package com.androidghost77.cryptocalculator.services

import com.androidghost77.cryptocalculator.models.UserDto
import com.androidghost77.cryptocalculator.repos.entities.User

interface UserService {
    fun getCurrentUser(): User

    fun getCurrentUserDto(): UserDto
}