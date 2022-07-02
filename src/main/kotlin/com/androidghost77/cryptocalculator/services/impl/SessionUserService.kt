package com.androidghost77.cryptocalculator.services.impl

import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.security.model.UserPrincipal
import com.androidghost77.cryptocalculator.services.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SessionUserService : UserService {
    override fun getCurrentUser(): User {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        return userPrincipal.user
    }
}