package com.ezzy.missingpersontracker.data.remote

import com.ezzy.core.data.datasource.MissingPersonDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.AdditionalContact
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.MissingPerson
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.ADDITIONAL_CONTACTS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.ADDRESS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.LOCATION
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.MISSING_PERSON_COLLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Suppress("SpellCheckingInspection")
class MissingPersonRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : MissingPersonDataSource {

    private val missingPersonCollection =
        firebaseFirestore.collection(MISSING_PERSON_COLLLECTION)

    override suspend fun addAMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        location: Location,
        additionalContact: AdditionalContact
    ): Boolean {
        return try {
            var isMissingPersonSaved = false
            missingPersonCollection.add(missingPerson).addOnSuccessListener { docReference ->
                CoroutineScope(Dispatchers.IO).launch {
                    val isAddressSaved = saveAddress(docReference.id, address)
                    val isLocationSaved = saveLocation(docReference.id, location)
                    val isAdditionalContactsSaved =
                        saveAdditionalContacts(docReference.id, additionalContact)

                    if (isAddressSaved && isLocationSaved && isAdditionalContactsSaved) {
                        isMissingPersonSaved = true
                    }
                }
            }.addOnFailureListener { isMissingPersonSaved = false }.await()
            isMissingPersonSaved
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun saveAddress(documentId: String, address: Address): Boolean {
        return try {
            var isAddressSaved = false
            missingPersonCollection.document(documentId)
                .collection(ADDRESS)
                .add(address)
                .addOnSuccessListener {
                    isAddressSaved = true
                }.addOnFailureListener { isAddressSaved = false }
                .await()
            isAddressSaved
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun saveLocation(documentId: String, location: Location): Boolean {
        return try {
            var isLocationSaved = false
            missingPersonCollection.document(documentId)
                .collection(LOCATION)
                .add(location)
                .addOnCompleteListener {
                    if (it.isSuccessful) isLocationSaved = true
                }.addOnFailureListener { isLocationSaved = false }
                .await()
            isLocationSaved
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun saveAdditionalContacts(
        documentId: String,
        additionalContact: AdditionalContact
    ): Boolean {
        return try {
            var isAdditionalContactSaved = false
            missingPersonCollection.document(documentId)
                .collection(ADDITIONAL_CONTACTS)
                .add(additionalContact)
                .addOnCompleteListener {
                    if (it.isSuccessful) isAdditionalContactSaved = true
                }.addOnFailureListener { isAdditionalContactSaved = false }
                .await()
            isAdditionalContactSaved
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun searchMissingPerson(name: String): Resource<Flow<List<MissingPerson>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMissingPeople(): Resource<Flow<List<MissingPerson>>> {
        val missingPersons = mutableListOf<MissingPerson>()
        val results = missingPersonCollection.get().await()
        results.forEach {
            missingPersons.add(it.toObject(MissingPerson::class.java))
        }
        return Resource.Success(flow {
            emit(missingPersons)
        }.flowOn(Dispatchers.IO))
    }

}