package com.ezzy.core.interactors

import com.ezzy.core.data.repository.MissingPersonRepository
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.*
import kotlinx.coroutines.flow.Flow
import java.net.URI

class AddMissingPerson(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
        missingPerson: MissingPerson,
        address: Address,
        contactList: List<Contact>,
        missingPersonImages: List<URI>,
        fileNames: List<String>
    ) = repository.addAMissingPerson(
        missingPerson,
        address,
        contactList,
        missingPersonImages,
        fileNames
    )
}

class SearchMissingPerson(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
        name: String
    ) = repository.searchMissingPerson(name)
}

class GetMissingPeople(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> =
        repository.getMissingPeople()
}

class GetMissingPersonId(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
        missingPerson: MissingPerson
    ): Flow<Resource<String>> = repository.getMissingPersonId(missingPerson)
}

class GetMissingPersonImages(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
        missingPerson: MissingPerson
    ): Flow<Resource<List<Image>>> = repository.getMissingPersonImages(missingPerson)
}