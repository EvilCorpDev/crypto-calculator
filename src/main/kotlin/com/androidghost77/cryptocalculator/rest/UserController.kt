package com.androidghost77.cryptocalculator.rest

import com.androidghost77.cryptocalculator.enums.UserRole
import com.androidghost77.cryptocalculator.models.binance.BinanceToken
import com.androidghost77.cryptocalculator.models.UserDto
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.security.DbUserDetailsManager
import com.androidghost77.cryptocalculator.security.model.UserPrincipal
import com.androidghost77.cryptocalculator.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userDetailsManager: DbUserDetailsManager,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val userService: UserService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody userDto: UserDto) {
        val user = User(
            id = UUID.randomUUID().toString(),
            username = userDto.username,
            passwordHash = passwordEncoder.encode(userDto.password),
            email = userDto.email,
            role = UserRole.USER
        )
        userDetailsManager.createUser(UserPrincipal(user, emptyList()))
    }

    @GetMapping("/current")
    fun getCurrentUser(): UserDto = userService.getCurrentUserDto()

    @PostMapping("/token")
    fun connectToBinance(@RequestBody tokenDto: BinanceToken) {
        val dbUser = userDetailsManager.loadUserById(userService.getCurrentUser().id) as UserPrincipal
        val updatedUser = UserPrincipal(
            dbUser.user.copy(binanceToken = tokenDto.apiToken, secretBinanceKey = tokenDto.secretKey),
            emptyList()
        )

        userDetailsManager.updateUser(updatedUser)
    }

    @PostMapping("/{username}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun grantAdmin(@PathVariable username: String) {
        userDetailsManager.grantAdmin(username)
    }
}