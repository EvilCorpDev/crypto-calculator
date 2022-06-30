package com.androidghost77.cryptocalculator.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.provisioning.UserDetailsManager

class DbUserDetailsManager : UserDetailsManager {
    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }

    override fun createUser(user: UserDetails?) {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: UserDetails?) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(username: String?) {
        TODO("Not yet implemented")
    }

    override fun changePassword(oldPassword: String?, newPassword: String?) {
        TODO("Not yet implemented")
    }

    override fun userExists(username: String?): Boolean {
        TODO("Not yet implemented")
    }

    fun loadUserById(userId: String): UserDetails {
        TODO("Not yet implemented")

    }
}