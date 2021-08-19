package com.ezzy.core.data.repository

import com.ezzy.core.data.datasource.MissingPersonDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.AdditionalContact
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.MissingPerson
import kotlinx.coroutines.flow.Flow

class MissingPersonRepository(
    private val dataSource: MissingPersonDataSource
) {
    suspend fun addAMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        location: Location,
        additionalContact: AdditionalContact
    ): Boolean = dataSource.addAMissingPerson(missingPerson, address, location, additionalContact)

    suspend fun searchMissingPerson(name: String): Resource<Flow<List<MissingPerson>>> =
        dataSource.searchMissingPerson(name)

    suspend fun getMissingPeople(): Resource<Flow<List<MissingPerson>>> = dataSource.getMissingPeople()
}