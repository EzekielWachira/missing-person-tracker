package com.ezzy.core.data.repository

import com.ezzy.core.data.datasource.UserDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.User
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val dataSource: UserDataSource
) {
    suspend fun addUser(
        user: User,
        address: Address,
        location: Location
    ): Flow<Resource<String>> = dataSource.addUser(user, address, location)
    suspend fun searchUser(userName: String): Flow<Resource<List<User>>> = dataSource.searchUser(userName)
    suspend fun getUserDetails(email: String): Flow<User> = dataSource.getUserDetails(email)
    suspend fun registerUser(email: String, password: String): Resource<Flow<Boolean>> =
        dataSource.registerUser(email, password)
    suspend fun loginUser(email: String, password: String): Boolean =
        dataSource.loginUser(email, password)
    suspend fun checkUser(email: String?, phoneNumber: String?): Flow<Resource<User>> =
        dataSource.checkUser(email, phoneNumber)
    suspend fun getAuthenticatedUserID(email: String?, phoneNumber: String?):
            Flow<Resource<String>> = dataSource.getAuthenticatedUserID(email, phoneNumber)
    suspend fun getMissingPersonReporter(userId: String): Flow<Resource<User>> =
        dataSource.getMissingPersonReporter(userId)
    suspend fun getAllUser(): Flow<Resource<List<User>>> = dataSource.getAllUser()
    suspend fun getReporterId(email: String?, phoneNumber: String?): Flow<Resource<String>> =
        dataSource.getReporterId(email, phoneNumber)
}