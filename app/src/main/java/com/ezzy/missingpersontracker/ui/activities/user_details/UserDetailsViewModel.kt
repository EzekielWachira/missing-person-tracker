package com.ezzy.missingpersontracker.ui.activities.user_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.User
import com.ezzy.core.interactors.AddUser
import com.ezzy.core.interactors.GetMissingPersonReporter
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val addUser: AddUser,
) : ViewModel() {

    private var _userId = MutableStateFlow<Resource<String>>(Resource.Empty)
    val userId: StateFlow<Resource<String>> get() = _userId

    fun saveUserDetails(user: User, address: Address, location: Location) {
        viewModelScope.launch {
            addUser(user, address, location).collect { resourceState ->
                when (resourceState) {
                    is Resource.Loading -> _userId.value = Resource.loading()
                    is Resource.Success -> _userId.value = Resource.success(resourceState.data)
                    is Resource.Failure -> _userId.value =
                        Resource.failed(resourceState.errorMessage!!)
                }
            }
        }
    }

}