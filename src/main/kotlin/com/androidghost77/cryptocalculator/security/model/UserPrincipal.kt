package com.androidghost77.cryptocalculator.security.model

import com.androidghost77.cryptocalculator.repos.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserPrincipal(
    val user: User,
    val userAuthorities: List<GrantedAuthority>,
) : UserDetails {
    override fun getAuthorities(): List<GrantedAuthority> = userAuthorities

    override fun getPassword(): String = user.passwordHash

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean  = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
