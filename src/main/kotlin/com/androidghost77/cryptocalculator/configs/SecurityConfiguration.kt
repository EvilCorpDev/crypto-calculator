package com.androidghost77.cryptocalculator.configs

import com.androidghost77.cryptocalculator.filter.JwtAuthenticationFilter
import com.androidghost77.cryptocalculator.mappers.UserMapper
import com.androidghost77.cryptocalculator.repos.UserRepository
import com.androidghost77.cryptocalculator.security.DbUserDetailsManager
import com.androidghost77.cryptocalculator.security.JwtAuthenticationEntryPoint
import com.androidghost77.cryptocalculator.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint

@Configuration
class SecurityConfiguration {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsManager(
        userMapper: UserMapper, userRepo: UserRepository?, passwordEncoder: BCryptPasswordEncoder
    ): DbUserDetailsManager = DbUserDetailsManager(userMapper, userRepo, passwordEncoder)

    @Bean
    fun tokenProvider(
        @Value("\${jwt.secret}") jwtSecret: String,
        @Value("\${jwt.expirationInMs}") jwtExpirationInMs: Int
    ): JwtTokenProvider = JwtTokenProvider(jwtSecret, jwtExpirationInMs)

    @Bean
    fun jwtAuthenticationEntryPoint(): AuthenticationEntryPoint = JwtAuthenticationEntryPoint()

    @Bean
    fun jwtAuthenticationFilter(
        tokenProvider: JwtTokenProvider,
        userDetailsManager: DbUserDetailsManager
    ): JwtAuthenticationFilter = JwtAuthenticationFilter(tokenProvider, userDetailsManager)
}