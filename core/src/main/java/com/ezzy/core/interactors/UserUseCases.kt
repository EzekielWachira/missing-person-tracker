package com.ezzy.core.interactors

import com.ezzy.core.data.repository.UserRepository
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.User
import kotlinx.coroutines.flow.Flow

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
    ): Resource<Flow<Boolean>> = repository.registerUser(email, password)
}

class LoginUser(private val repository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.loginUser(email, password)
}

class CheckUser(private val repository: UserRepository) {
    suspend operator fun invoke(
        email: String?, phoneNumber: String?
    ): Flow<Resource<User>> = repository.checkUser(email, phoneNumber)
}

class GetAuthenticatedUserID(private val repository: UserRepository) {
    suspend operator fun invoke (
        email: String?, phoneNumber: String?
    ): Flow<Resource<String>> = repository.getAuthenticatedUserID(
        email, phoneNumber
    )
}

class GetMissingPersonReporter(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String):
            Flow<Resource<User>> = repository.getMissingPersonReporter(userId)
}

class GetAllUsers(private val repository: UserRepository) {
    suspend operator fun invoke(): Flow<Resource<List<User>>> =
        repository.getAllUser()
}

class GetReporterId(private val repository: UserRepository) {
    suspend operator fun invoke(
        email: String?,
        phoneNumber: String?
    ): Flow<Resource<String>> = repository.getReporterId(email, phoneNumber)
}

