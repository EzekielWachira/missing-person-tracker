package com.ezzy.core.data.datasource

import com.ezzy.core.domain.AdditionalContact
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.MissingPerson
import kotlinx.coroutines.flow.Flow

interface MissingPersonDataSource {
    suspend fun addAMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        location: Location,
        additionalContact: AdditionalContact
    )
    suspend fun searchMissingPerson(name: String): Flow<List<MissingPerson>>
    suspend fun getMissingPeople(): Flow<List<MissingPerson>>
}