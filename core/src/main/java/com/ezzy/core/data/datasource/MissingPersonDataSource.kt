package com.ezzy.core.data.datasource

import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.*
import kotlinx.coroutines.flow.Flow
import java.net.URI

/**
 * this interface contains the implementation logic of missing person
 * */
interface MissingPersonDataSource {

    /**
     * this function is the logic for adding a missin person
     * @param address
     * @param contactList
     * @param missingPersonImages
     * @param fileNames
     * */
    suspend fun addAMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        contactList: List<Contact>,
        missingPersonImages: List<URI>,
        fileNames: List<String>
    ): Flow<Resource<String>>

    /**
     * Search a missing person
     * @param name
     * */
    suspend fun searchMissingPerson(name: String): Resource<Flow<List<MissingPerson>>>

    /**
     * get missing people
     * */
    suspend fun getMissingPeople(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>>

    /**
     * get found people
     * */
    suspend fun getFoundPeople(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>>

    /**
     * get missing person id
     * @param missingPerson
     * */
    suspend fun getMissingPersonId(missingPerson: MissingPerson): Flow<Resource<String>>

    /**
     * get missing person images
     * @param missingPerson
     * */
    suspend fun getMissingPersonImages(missingPerson: MissingPerson): Flow<Resource<List<Image>>>

    /**
     * report found person
     * @param missingPerson
     * @param address
     * */
    suspend fun reportFoundPerson(missingPerson: MissingPerson, address: Address): Flow<Resource<String>>

}
