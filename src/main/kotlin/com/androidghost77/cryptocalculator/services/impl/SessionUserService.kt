package com.androidghost77.cryptocalculator.services.impl

import com.androidghost77.cryptocalculator.mappers.UserMapper
import com.androidghost77.cryptocalculator.models.UserDto
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.security.model.UserPrincipal
import com.androidghost77.cryptocalculator.services.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SessionUserService(
    val userMapper: UserMapper,
) : UserService {
    override fun getCurrentUser(): User {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        return userPrincipal.user
    }

    override fun getCurrentUserDto(): UserDto = userMapper.userToUserDto(getCurrentUser())
}