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

    suspend fun searchMissingPerson(
        name: String
    ): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> =
        dataSource.searchMissingPerson(name)

    suspend fun searchMissingPersonByFirstName(name: String): Flow<Resource<List<MissingPerson>>> =
        dataSource.searchMissingPersonByFirstName(name)

    suspend fun getMissingPeople(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> =
        dataSource.getMissingPeople()

    suspend fun getFoundPeople(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> =
        dataSource.getFoundPeople()

    suspend fun getMissingPersonId(missingPerson: MissingPerson): Flow<Resource<String>> =
        dataSource.getMissingPersonId(missingPerson)

    suspend fun getMissingPersonImages(missingPerson: MissingPerson): Flow<Resource<List<Image>>> =
        dataSource.getMissingPersonImages(missingPerson)

    suspend fun reportFoundPerson(
        missingPerson: MissingPerson,
        address: Address
    ): Flow<Resource<String>> =
        dataSource.reportFoundPerson(missingPerson, address)

    /**
     * get found person address
     * @param missingPersonId
     * */
    suspend fun getFoundPersonAddress(missingPersonId: String): Flow<Resource<Address>> =
        dataSource.getFoundPersonAddress(missingPersonId)
}