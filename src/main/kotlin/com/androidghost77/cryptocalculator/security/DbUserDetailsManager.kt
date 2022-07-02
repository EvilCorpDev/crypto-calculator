package com.androidghost77.cryptocalculator.security

import com.androidghost77.cryptocalculator.enums.UserRole
import com.androidghost77.cryptocalculator.exceptions.PasswordNotMatchException
import com.androidghost77.cryptocalculator.repos.UserRepository
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.security.model.UserPrincipal
import com.androidghost77.cryptocalculator.services.UserService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import java.nio.file.attribute.UserPrincipalNotFoundException

class DbUserDetailsManager(
    private val userRepo: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val userService: UserService,
) : UserDetailsManager {
    override fun loadUserByUsername(username: String): UserDetails? =
        userRepo.findByUsername(username)?.let {
            UserPrincipal(it, listOf(SimpleGrantedAuthority(it.role.name)))
        }

    override fun createUser(userDetails: UserDetails) {
        userRepo.save((userDetails as UserPrincipal).user)
    }

    override fun updateUser(userDetails: UserDetails) {
        userRepo.save((userDetails as UserPrincipal).user)
    }

    override fun deleteUser(username: String) = userRepo.deleteByUsername(username)

    override fun changePassword(oldPassword: String, newPassword: String) {
        val user: User = userService.getCurrentUser()
        if (user.passwordHash == passwordEncoder.encode(oldPassword)) {
            userRepo.save(user.copy(passwordHash = passwordEncoder.encode(newPassword)))
        } else {
            throw PasswordNotMatchException("Wrong old password")
        }
    }

    override fun userExists(username: String): Boolean = loadUserByUsername(username) != null

    fun loadUserById(userId: String): UserDetails =
        userRepo.findById(userId).map {
            UserPrincipal(it, listOf(SimpleGrantedAuthority(it.role.name)))
        }.orElseThrow { UserPrincipalNotFoundException("User with id: $userId not found") }

    fun grantAdmin(username: String) {
        val userDetails = (loadUserByUsername(username)
            ?: throw UsernameNotFoundException("Can't find user with username $username")) as UserPrincipal
        val updatedUser = userDetails.user.copy(role = UserRole.ADMIN)
        updateUser(UserPrincipal(updatedUser, emptyList()))
    }
}