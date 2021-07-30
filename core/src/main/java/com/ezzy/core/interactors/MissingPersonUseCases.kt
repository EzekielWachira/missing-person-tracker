package com.ezzy.core.interactors

import com.ezzy.core.data.repository.MissingPersonRepository
import com.ezzy.core.domain.AdditionalContact
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.MissingPerson

class AddMissingPerson(private val repository: MissingPersonRepository) {
    suspend operator fun invoke (
        missingPerson: MissingPerson,
        address: Address,
        location: Location,
        additionalContact: AdditionalContact
    ) = repository.addAMissingPerson(missingPerson, address, location, additionalContact)
}

class SearchMissingPerson(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
       name: String
    ) = repository.searchMissingPerson(name)
}

class GetMissingPeople(private val repository: MissingPersonRepository) {
    suspend operator fun invoke() = repository.getMissingPeople()
}