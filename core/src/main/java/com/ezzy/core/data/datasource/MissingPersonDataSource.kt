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
    ): Boolean
    suspend fun searchMissingPerson(name: String): Resource<Flow<List<MissingPerson>>>
    suspend fun getMissingPeople(): Resource<Flow<List<MissingPerson>>>
}