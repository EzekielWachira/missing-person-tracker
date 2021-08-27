package com.ezzy.core.data.datasource

import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.*
import kotlinx.coroutines.flow.Flow
import java.net.URI

interface MissingPersonDataSource {
    suspend fun addAMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        contactList: List<Contact>,
        missingPersonImages: List<URI>,
        fileNames: List<String>
    ): Flow<Resource<String>>
    suspend fun searchMissingPerson(name: String): Resource<Flow<List<MissingPerson>>>
    suspend fun getMissingPeople(): Flow<Resource<List<MissingPerson>>>
}