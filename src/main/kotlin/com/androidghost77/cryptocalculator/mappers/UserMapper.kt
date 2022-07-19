package com.androidghost77.cryptocalculator.mappers

import com.androidghost77.cryptocalculator.models.UserDto
import com.androidghost77.cryptocalculator.repos.entities.User
import com.androidghost77.cryptocalculator.security.model.UserPrincipal
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {
    fun userPrincipalToUser(userRequest: UserPrincipal): User

    @Mapping(target = "connectedToBinance", expression = "java(user.getBinanceToken() != null)")
    @Mapping(target = "password", expression = "java(\"\")")
    fun userToUserDto(user: User): UserDto
}