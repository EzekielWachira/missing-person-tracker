package com.ezzy.missingpersontracker.ui.activities.user_details

import androidx.lifecycle.ViewModel
import com.ezzy.core.interactors.AddUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val addUser: AddUser
): ViewModel(){



}