package com.ezzy.core.data.repository

import com.ezzy.core.data.datasource.UserDataSource
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
    ) = dataSource.addUser(user, address, location)
    suspend fun searchUser(userName: String): Flow<List<User>> = dataSource.searchUser(userName)
    suspend fun getUserDetails(email: String): Flow<User> = dataSource.getUserDetails(email)
}