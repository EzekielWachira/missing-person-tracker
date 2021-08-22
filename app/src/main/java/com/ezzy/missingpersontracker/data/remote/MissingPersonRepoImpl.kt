package com.ezzy.missingpersontracker.data.remote

import android.net.Uri
import com.ezzy.core.data.datasource.MissingPersonDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.*
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.ADDITIONAL_CONTACTS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.ADDRESS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.IMAGES
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.LOCATION
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.MISSING_PERSON_COLLLECTION
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.MISSING_PERSON_IMAGES_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.net.URI
import javax.inject.Inject

@Suppress("SpellCheckingInspection")
class MissingPersonRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : MissingPersonDataSource {

    private val missingPersonCollection =
        firebaseFirestore.collection(MISSING_PERSON_COLLLECTION)
    private val storageReference = firebaseStorage.reference

    override suspend fun addAMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        contactList: List<Contact>,
        missingPersonImages: List<URI>
    ): Boolean {
        return try {
            var isMissingPersonSaved = false
            missingPersonCollection.add(missingPerson).addOnSuccessListener { docReference ->
                CoroutineScope(Dispatchers.IO).launch {
                    val isAddressSaved = saveAddress(docReference.id, address)
                    val isContactListSaved = saveContactList(docReference.id, contactList)
                    val areImagesSaved = saveImageLinks(
                        docReference.id,
                        saveMissingPersonImages(
                            missingPersonImages ,
                            missingPerson.reporterId!!
                        )
                    )

                    if (isAddressSaved && isContactListSaved && areImagesSaved)
                        isMissingPersonSaved = true
                }
            }.addOnFailureListener { isMissingPersonSaved = false }.await()
            isMissingPersonSaved
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun saveImageLinks(missingPersonId: String, links: List<String>): Boolean {
        return try {
            var areImageLinksSaved = false
            for (link in links){
                firebaseFirestore.collection(MISSING_PERSON_IMAGES_COLLECTION)
                    .document(missingPersonId)
                    .collection(IMAGES)
                    .add(link)
                    .addOnSuccessListener { areImageLinksSaved = true }
                    .addOnFailureListener { areImageLinksSaved = false }
                    .await()
            }
            areImageLinksSaved
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun saveMissingPersonImages(
        missingPersonImages: List<URI>,
        reporterId: String
    ): List<String> {
        return try {
            val missingPersonImagePathList = mutableListOf<String>()
            val reference = storageReference.child("images/missing_persons/$reporterId")
            missingPersonImages.forEach { mspImageUri ->
                val imageUri = Uri.parse(mspImageUri.toString())
                reference.putFile(imageUri)
                    .addOnSuccessListener {
                        reference.downloadUrl
                            .addOnSuccessListener { uri ->
                                missingPersonImagePathList.add(uri.toString())
                            }
                    }.addOnFailureListener {
                        it.printStackTrace()
                    }.await()
            }
            missingPersonImagePathList
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun saveContactList(documentId: String, contactList: List<Contact>): Boolean {
        return try {
            var isContactListSaved = false
            missingPersonCollection.document(documentId)
                .collection(LOCATION)
                .add(contactList)
                .addOnSuccessListener {
                    isContactListSaved = true
                }.addOnFailureListener { isContactListSaved = false }
                .await()
            isContactListSaved
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

    private fun savePersonImages() {

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