package com.ezzy.core.data.datasource

import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    suspend fun addUser(
        user: User,
        address: Address,
        location: Location
    )
    suspend fun searchUser(userName: String): Flow<List<User>>
    suspend fun getUserDetails(email: String): Flow<User>
    suspend fun registerUser(email: String, password: String): Resource<Flow<Boolean>>
    suspend fun loginUser(email: String, password: String): Boolean
}