package com.ezzy.missingpersontracker.data.remote

import com.ezzy.core.data.datasource.UserDataSource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.util.Constants.FIRESTORE_COLLECTIONS.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : UserDataSource {

    override suspend fun registerUser(email: String, password: String): Boolean {
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

        return isRegistrationSuccess
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

    override suspend fun addUser(user: User, address: Address, location: Location) {
        try {
            firebaseFirestore.collection(USER_COLLECTION)
                .add(user)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Timber.d("User saved")
                    }
                }.addOnFailureListener { Timber.d(it) }
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun searchUser(userName: String): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDetails(email: String): Flow<User> {
        TODO("Not yet implemented")
    }
}