package com.ezzy.missingpersontracker.data.remote

import android.net.Uri
import com.ezzy.core.data.datasource.MissingPersonDataSource
import com.ezzy.core.data.datasource.MissingPersonImageDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.*
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.ADDITIONAL_CONTACTS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.ADDRESS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.CONTACTS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.IMAGES
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.LOCATION
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.MISSING_PERSON_COLLLECTION
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.MISSING_PERSON_IMAGES_COLLECTION
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.USER_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.net.URI
import javax.inject.Inject

@Suppress("SpellCheckingInspection")
class MissingPersonRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage
) : MissingPersonDataSource, MissingPersonImageDataSource {

    private val missingPersonCollection =
        firebaseFirestore.collection(MISSING_PERSON_COLLLECTION)
    private val storageReference = firebaseStorage.reference
    private val imagesCollection = firebaseFirestore.collection(MISSING_PERSON_IMAGES_COLLECTION)
    private val userCollection = firebaseFirestore.collection(USER_COLLECTION)
    private var missingPersonImages: List<Image>? = null

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
                .add(Image(link)).await()
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
        val reference =
            storageReference.child("images/missing_persons/$reporterId/${System.currentTimeMillis()}_$fileName")
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

    override suspend fun getMissingPeople(): Flow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> =
        flow {
            emit(Resource.loading())
            var images: List<Image>? = null
            var reporter: User? = null
            val pairList = mutableListOf<Pair<Pair<MissingPerson, List<Image>>, User>>()
            val snapshot = missingPersonCollection.get().await()
            snapshot.forEach { doc ->
                val missingPerson = doc.toObject(MissingPerson::class.java)
                getImages(doc.id).collect { state ->
                    when (state) {
                        is Resource.Loading -> Timber.i("Loading data")
                        is Resource.Success -> {
                            images = state.data
                        }
                        is Resource.Failure -> Timber.e("Error getting missing people: ${state.errorMessage}")
                        is Resource.Empty -> Timber.e("Could not get missing person images: EMPTY!!")
                    }
                }

                getReporter(missingPerson.reporterId!!).collect { state ->
                    when (state) {
                        is Resource.Loading -> Timber.i("Loading data")
                        is Resource.Success -> {
                            reporter = state.data
                        }
                        is Resource.Failure -> Timber.e("Error getting missing people: ${state.errorMessage}")
                        is Resource.Empty -> Timber.e("Could not get missing person images: EMPTY!!")
                    }
                }

                pairList.add(Pair(Pair(missingPerson, images!!), reporter!!))
            }
            emit(Resource.success(pairList))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)

    private suspend fun getReporter(reporterId: String): Flow<Resource<User>> = flow {
        emit(Resource.loading())
        val snapshot = userCollection.document(reporterId).get().await()
        val user = snapshot.toObject(User::class.java)!!
        emit(Resource.success(user))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    private suspend fun getImages(personId: String): Flow<Resource<List<Image>>> =
        flow {
            emit(Resource.loading())
            val userImages = mutableListOf<Image>()
            val snapshot = imagesCollection.document(personId).collection(IMAGES).get().await()
            snapshot.forEach { snap ->
                userImages.add(snap.toObject(Image::class.java))
            }
            missingPersonImages = userImages
            emit(Resource.success(userImages))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)


    override suspend fun getPersonImages(personId: String): Flow<Resource<List<Image>>> =
        flow {
            emit(Resource.loading())
            val userImages = mutableListOf<Image>()
            val snapshot = imagesCollection.document(personId).collection(IMAGES).get().await()
            snapshot.forEach { snap ->
                userImages.add(snap.toObject(Image::class.java))
            }
            emit(Resource.success(userImages))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)


    override suspend fun getMissingPersonId(missingPerson: MissingPerson): Flow<Resource<String>> =
        flow {
            emit(Resource.loading())
            val snapshot = missingPersonCollection.get().await()
            var mspId: String? = null
            snapshot.forEach { snap ->
                if ((snap.toObject(MissingPerson::class.java)) == missingPerson) {
                    mspId = snap.id
                }
            }
            emit(Resource.success(mspId!!))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)


    override suspend fun getMissingPersonImages(missingPerson: MissingPerson): Flow<Resource<List<Image>>> =
        flow {
            emit(Resource.loading())
            val snapshot = missingPersonCollection.get().await()
            var mspId: String? = null
            snapshot.forEach { snap ->
                if ((snap.toObject(MissingPerson::class.java)) == missingPerson) {
                    mspId = snap.id
                }
            }
            val images = mutableListOf<Image>()
            val imagesSnapshot = imagesCollection.document(mspId!!).collection(IMAGES).get().await()
            imagesSnapshot.forEach { snap ->
                images.add(snap.toObject(Image::class.java))
            }
            emit(Resource.success(images))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)
}