package com.androidghost77.cryptocalculator.services.impl

import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.services.UserService
import org.springframework.stereotype.Service

@Service
class SessionUserService : UserService {
    override fun getCurrentUser(): User {
        //SecurityContextHolder.getContext().authentication.principal
        TODO("Not yet implemented")
    }
}