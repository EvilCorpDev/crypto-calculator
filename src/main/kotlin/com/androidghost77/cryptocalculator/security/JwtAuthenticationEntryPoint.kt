package com.androidghost77.cryptocalculator.security

import com.androidghost77.cryptocalculator.services.Logging
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        logger().error("Unauthorized exception, message {}", authException!!.message)
        response!!.sendError(HttpStatus.UNAUTHORIZED.value(), authException.message)
    }

    companion object : Logging
}