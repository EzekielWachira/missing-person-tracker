package com.ezzy.missingpersontracker.data.remote

import com.ezzy.core.data.datasource.UserDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.ADDRESS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.LOCATION
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : UserDataSource {

    private val userCollection = firebaseFirestore.collection(USER_COLLECTION)

    override suspend fun checkUser(email: String?, phoneNumber: String?): Flow<Resource<User>> =
        flow {
            emit(Resource.loading())
            var user: User? = null
            var snapshot: QuerySnapshot? =  null
            val userColl = firebaseFirestore.collection(USER_COLLECTION)
            if (email != null) {
                snapshot = userColl.whereEqualTo("email", email).get().await()
            } else if (phoneNumber != null) {
                snapshot = userColl.whereEqualTo("phoneNumber", phoneNumber).get().await()
            }

            snapshot?.forEach { snap ->
                user = snap.toObject(User::class.java)
            }

            emit(Resource.success(user!!))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)


    /**
     * Register user to firestore cloud storage
     * */
    override suspend fun getAuthenticatedUserID(
        email: String?,
        phoneNumber: String?
    ): Flow<Resource<String>> = flow {
        emit(Resource.loading())
        var userId: String? = null
        var snapshot: QuerySnapshot? =  null
        val userColl = firebaseFirestore.collection(USER_COLLECTION)
        if (email != null) {
            snapshot = userColl.whereEqualTo("email", email).get().await()
        } else if (phoneNumber != null) {
            snapshot = userColl.whereEqualTo("phoneNumber", phoneNumber).get().await()
        }
        snapshot?.forEach { snap ->
            userId = snap.id
        }
        emit(Resource.success(userId!!))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)


    /**
     * Register user to firestore cloud storage
     * */
    override suspend fun registerUser(email: String, password: String): Resource<Flow<Boolean>> {
        return try {
            var isRegistrationSuccess = false
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isRegistrationSuccess = true
                        val firebaseUser = task.result?.user
                        Timber.d("$firebaseUser")
                    }
                }.addOnFailureListener { exception ->
                    isRegistrationSuccess = false
                    Timber.d(exception)
                }.await()
            Resource.Success(flow { emit(isRegistrationSuccess) })
        } catch (e: Exception) {
            Resource.Failure("An exception occurred")
        }
    }

    /**
     * login user
     * */
    override suspend fun loginUser(email: String, password: String): Boolean {
        var isLoginSuccess = false
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isLoginSuccess = true
                    }
                }.addOnFailureListener { isLoginSuccess = false }
                .await()
        } catch (e: Exception) {
            isLoginSuccess = false
            e.printStackTrace()
        }

        return isLoginSuccess
    }

    override suspend fun addUser(
        user: User,
        address: Address,
        location: Location
    ): Flow<Resource<String>> = flow {
        emit(Resource.loading())
        val userRef = firebaseFirestore.collection(USER_COLLECTION).add(user).await().apply {
            saveUserAddress(address, this.id)
            saveUserLocation(location, this.id)
        }
        emit(Resource.success(userRef.id))
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)
//    {
//        try {
//            firebaseFirestore.collection(USER_COLLECTION)
//                .add(user)
//                .addOnSuccessListener {
////                    if (it.isSuccessful) {
////                        Timber.d("User saved")
////                    }
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val isUserAddressSaved = saveUserAddress(address, it.id)
//                        val isUserLocationSaved = saveUserLocation(location, it.id)
//                    }
//                }.addOnFailureListener { Timber.d(it) }
//                .await()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    private suspend fun saveUserAddress(
        address: Address,
        documentId: String
    ): Flow<Resource<String>> =
        flow {
            emit(Resource.loading())
            val addressRef = firebaseFirestore.collection(USER_COLLECTION)
                .document(documentId)
                .collection(ADDRESS)
                .add(address).await()
            emit(Resource.success(addressRef.id))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)

//        {
//            return try {
//                var isUserAddressSaved = false
//                firebaseFirestore.collection(USER_COLLECTION)
//                    .document(documentId)
//                    .collection(ADDRESS)
//                    .add(address)
//                    .addOnSuccessListener {
//                        isUserAddressSaved = true
//                    }.addOnFailureListener { isUserAddressSaved = false }
//                    .await()
//                isUserAddressSaved
//            } catch (e: Exception) {
//                e.printStackTrace()
//                false
//            }
//        }

    private suspend fun saveUserLocation(location: Location, documentId: String): Boolean {
        return try {
            var isUserLocationSaved = false
            firebaseFirestore.collection(USER_COLLECTION)
                .document(documentId)
                .collection(LOCATION)
                .add(location)
                .addOnSuccessListener { isUserLocationSaved = true }
                .addOnFailureListener { isUserLocationSaved = false }
                .await()
            isUserLocationSaved
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getMissingPersonReporter(userId: String): Flow<Resource<User>> =
        flow {
            emit(Resource.loading())
            val snapshot = userCollection.document(userId).get().await()
            val user = snapshot.toObject(User::class.java)
            emit(Resource.success(user!!))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)

    override suspend fun searchUser(userName: String): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDetails(email: String): Flow<User> {
        TODO("Not yet implemented")
    }
}