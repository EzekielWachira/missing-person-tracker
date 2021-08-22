package com.ezzy.core.data.repository

import com.ezzy.core.data.datasource.MissingPersonDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.*
import kotlinx.coroutines.flow.Flow
import java.net.URI

class MissingPersonRepository(
    private val dataSource: MissingPersonDataSource
) {
    suspend fun addAMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        contactList: List<Contact>,
        missingPersonImages: List<URI>
    ): Boolean =
        dataSource.addAMissingPerson(missingPerson, address, contactList, missingPersonImages)

    suspend fun searchMissingPerson(name: String): Resource<Flow<List<MissingPerson>>> =
        dataSource.searchMissingPerson(name)

    suspend fun getMissingPeople(): Resource<Flow<List<MissingPerson>>> =
        dataSource.getMissingPeople()
}