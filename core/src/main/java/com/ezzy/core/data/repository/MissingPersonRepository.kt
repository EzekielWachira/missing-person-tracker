package com.ezzy.core.data.repository

import com.ezzy.core.data.datasource.MissingPersonDataSource
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
    ) = dataSource.addAMissingPerson(missingPerson, address, location, additionalContact)
    suspend fun searchMissingPerson(name: String): Flow<List<MissingPerson>> =
        dataSource.searchMissingPerson(name)
    suspend fun getMissingPeople(): Flow<List<MissingPerson>> = dataSource.getMissingPeople()
}