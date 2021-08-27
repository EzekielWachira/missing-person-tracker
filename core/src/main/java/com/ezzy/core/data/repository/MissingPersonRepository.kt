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
        missingPersonImages: List<URI>,
        fileNames: List<String>
    ): Flow<Resource<String>> =
        dataSource.addAMissingPerson(
            missingPerson,
            address,
            contactList,
            missingPersonImages,
            fileNames
        )

    suspend fun searchMissingPerson(name: String): Resource<Flow<List<MissingPerson>>> =
        dataSource.searchMissingPerson(name)

    suspend fun getMissingPeople(): Flow<Resource<List<MissingPerson>>> =
        dataSource.getMissingPeople()
}