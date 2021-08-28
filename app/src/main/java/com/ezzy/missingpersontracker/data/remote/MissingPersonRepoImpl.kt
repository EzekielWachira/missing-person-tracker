package com.ezzy.missingpersontracker.data.remote

import android.net.Uri
import com.ezzy.core.data.datasource.MissingPersonDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.*
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.ADDITIONAL_CONTACTS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.ADDRESS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.CONTACTS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.IMAGES
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.LOCATION
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.MISSING_PERSON_COLLLECTION
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.MISSING_PERSON_IMAGES_COLLECTION
import com.ezzy.missingpersontracker.util.getNameFromUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.net.URI
import javax.inject.Inject

@Suppress("SpellCheckingInspection")
class MissingPersonRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage
) : MissingPersonDataSource {

    private val missingPersonCollection =
        firebaseFirestore.collection(MISSING_PERSON_COLLLECTION)
    private val storageReference = firebaseStorage.reference

    override suspend fun addAMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        contactList: List<Contact>,
        missingPersonImages: List<URI>,
        fileNames: List<String>
    ): Flow<Resource<String>> = flow {
        emit(Resource.loading())
        val missingPersonRef = missingPersonCollection.add(missingPerson).await().apply {
            saveAddress(this.id, address).collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        Timber.i("Saving address")
                    }
                    is Resource.Success -> {
                        Timber.i("Address id: ${state.data}")
                    }
                    is Resource.Failure -> {
                        Timber.e("Address not saved")
                    }
                }
            }
            saveContactList(this.id, contactList).collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        Timber.i("Saving contacts")
                    }
                    is Resource.Success -> {
                        Timber.i("COntacts status: ${state.data}")
                    }
                    is Resource.Failure -> {
                        Timber.e("Contacts not saved")
                    }
                }
            }
            saveMissingPersonImages(
                missingPersonImages,
                missingPerson.reporterId ?: "23",
                fileNames
            ).collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        Timber.i("Saving images")
                    }
                    is Resource.Success -> {
                        saveImageLinks(this.id, state.data).collect { imageState ->
                            when (imageState) {
                                is Resource.Loading -> {
                                    Timber.i("Saving contacts")
                                }
                                is Resource.Success -> {
                                    Timber.i("Image status status: ${imageState.data}")
                                }
                                is Resource.Failure -> {
                                    Timber.e("Contacts not saved")
                                }
                            }
                        }
                    }
                    is Resource.Failure -> {
                        Timber.e("images not saved")
                    }
                }
            }
        }
        emit(Resource.success(missingPersonRef.id))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    private suspend fun saveImageLinks(
        missingPersonId: String,
        links: List<String>
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading())
        for (link in links) {
            firebaseFirestore.collection(MISSING_PERSON_IMAGES_COLLECTION)
                .document(missingPersonId)
                .collection(IMAGES)
                .add(mapOf("image_src" to link)).await()
        }
        emit(Resource.success(true))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    private suspend fun saveMissingPersonImages(
        missingPersonImages: List<URI>,
        reporterId: String,
        fileNames: List<String>
    ): Flow<Resource<List<String>>> = flow {
        emit(Resource.loading())
        val missingPersonImagePathList = mutableListOf<String>()
        for (i in missingPersonImages.indices) {
            processImage(
                missingPersonImages[i],
                fileNames[i],
                reporterId
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Timber.i("Processing image")
                    }
                    is Resource.Success -> {
                        Timber.d("IMAGES DATA: ${resource.data}")
                        missingPersonImagePathList.add(resource.data)
                    }
                    is Resource.Failure -> {
                        Timber.e("Image not processed")
                    }
                }
            }
        }
        Timber.d("IMAGESSS: $missingPersonImagePathList")
        emit(Resource.success(missingPersonImagePathList))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    private fun processImage(
        uri: URI,
        fileName: String,
        reporterId: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.loading())
        val imageUri = Uri.parse(uri.toString())
        val reference = storageReference.child("images/missing_persons/$reporterId/$fileName")
        reference.putFile(imageUri).await()
        val downloadUrl = reference.downloadUrl.await()
        emit(Resource.success(downloadUrl.toString()))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    private suspend fun saveContactList(
        documentId: String,
        contactList: List<Contact>
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading())
        for (contact in contactList) {
            missingPersonCollection.document(documentId)
                .collection(CONTACTS)
                .add(contact).await()
        }
        emit(Resource.success(true))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    private suspend fun saveAddress(documentId: String, address: Address): Flow<Resource<String>> =
        flow {
            emit(Resource.loading())
            val addressRef = missingPersonCollection.document(documentId)
                .collection(ADDRESS)
                .add(address).await()
            emit(Resource.success(addressRef.id))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)

    private suspend fun saveLocation(
        documentId: String,
        location: Location
    ): Flow<Resource<String>> = flow {
        emit(Resource.loading())
        val locationSnapshot = missingPersonCollection.document(documentId)
            .collection(LOCATION)
            .add(location).await()
        emit(Resource.success(locationSnapshot.id))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

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

    override suspend fun getMissingPeople(): Flow<Resource<List<MissingPerson>>> =
        flow<Resource<List<MissingPerson>>> {
            emit(Resource.loading())
            val snapshot = missingPersonCollection.get().await()
            val missingPersons = snapshot.toObjects(MissingPerson::class.java)
            emit(Resource.success(missingPersons))
        }.catch {
            emit(Resource.failed(it.toString()))
        }.flowOn(Dispatchers.IO)
}