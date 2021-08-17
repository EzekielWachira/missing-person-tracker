package com.ezzy.core.interactors

import com.ezzy.core.data.repository.UserRepository
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.User

class AddUser(private val userRepository: UserRepository) {
    suspend operator fun invoke (
        user: User,
        address: Address,
        location: Location
    ) = userRepository.addUser(user, address, location)
}

class SearchUser(private val repository: UserRepository) {
    suspend operator fun invoke(
        userName: String
    ) = repository.searchUser(userName)
}

class GetUserDetails(private val repository: UserRepository) {
    suspend operator fun invoke (
        email: String
    ) = repository.getUserDetails(email)
}

class RegisterUser(private val repository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.registerUser(email, password)
}

class LoginUser(private val repository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.loginUser(email, password)
}

