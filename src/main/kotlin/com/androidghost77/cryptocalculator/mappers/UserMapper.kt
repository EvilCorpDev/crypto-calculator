package com.androidghost77.cryptocalculator.mappers

import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.security.model.UserPrincipal
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserMapper {
    fun userPrincipalToUser(userRequest: UserPrincipal): User
}