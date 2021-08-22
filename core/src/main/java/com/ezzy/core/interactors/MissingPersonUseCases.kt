package com.ezzy.core.interactors

import com.ezzy.core.data.repository.MissingPersonRepository
import com.ezzy.core.domain.*
import java.net.URI

class AddMissingPerson(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
        missingPerson: MissingPerson,
        address: Address,
        contactList: List<Contact>,
        missingPersonImages: List<URI>
    ) = repository.addAMissingPerson(missingPerson, address, contactList, missingPersonImages)
}

class SearchMissingPerson(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
        name: String
    ) = repository.searchMissingPerson(name)
}

class GetMissingPeople(private val repository: MissingPersonRepository) {
    suspend operator fun invoke() = repository.getMissingPeople()
}