package com.ezzy.core.interactors

import com.ezzy.core.data.repository.MissingPersonRepository
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.*
import kotlinx.coroutines.flow.Flow
import java.net.URI

/**
 * Add missing person single usecase
 * @param repository
 * A usecase is a single operation a user can conduct for example adding a missing person
 * */
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

/**
 * search missing person usecase
 * @param repository
 * */
class SearchMissingPerson(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
        name: String
    ): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> =
        repository.searchMissingPerson(name)
}

class SearchMissingPersonByFirstName(
    private val repository: MissingPersonRepository
){
    suspend operator fun invoke(
        name: String
    ): Flow<Resource<List<MissingPerson>>> = repository.searchMissingPersonByFirstName(name)
}

class GetMissingPeople(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> =
        repository.getMissingPeople()
}

class GetFoundPeople(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> =
        repository.getFoundPeople()
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

class ReportFoundPerson(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
        missingPerson: MissingPerson,
        address: Address
    ): Flow<Resource<String>> = repository.reportFoundPerson(missingPerson, address)
}

class GetFoundPersonAddress(private val repository: MissingPersonRepository) {
    suspend operator fun invoke(
        missingPersonId: String
    ): Flow<Resource<Address>> = repository.getFoundPersonAddress(missingPersonId)
}