package com.androidghost77.cryptocalculator.configs

import com.androidghost77.cryptocalculator.filter.JwtAuthenticationFilter
import com.androidghost77.cryptocalculator.repos.UserRepository
import com.androidghost77.cryptocalculator.security.DbUserDetailsManager
import com.androidghost77.cryptocalculator.security.JwtAuthenticationEntryPoint
import com.androidghost77.cryptocalculator.security.JwtTokenProvider
import com.androidghost77.cryptocalculator.services.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun userDetailsManager(
        userRepo: UserRepository, passwordEncoder: BCryptPasswordEncoder, userService: UserService
    ): DbUserDetailsManager = DbUserDetailsManager(userRepo, passwordEncoder, userService)

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

    @Bean
    fun filterChain(
        http: HttpSecurity,
        authenticationEntryPoint: AuthenticationEntryPoint,
        authFilter: JwtAuthenticationFilter,
    ): SecurityFilterChain? {
        http.cors().and().csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/auth/role", "/operations/**")
            .authenticated()
            .and().headers().frameOptions().sameOrigin()
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}