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

    override suspend fun searchUser(userName: String): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDetails(email: String): Flow<User> {
        TODO("Not yet implemented")
    }
}