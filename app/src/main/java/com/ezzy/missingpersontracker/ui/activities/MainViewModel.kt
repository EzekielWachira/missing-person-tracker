package com.ezzy.missingpersontracker.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.User
import com.ezzy.core.interactors.CheckUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkUser: CheckUser
) : ViewModel() {

    private var _user = MutableStateFlow<Resource<User>>(Resource.Empty)
    val user: StateFlow<Resource<User>> get() = _user

    fun checkAuthenticatedUser(email: String?, phoneNumber: String?) =
        viewModelScope.launch {
            checkUser(email, phoneNumber).collect { state ->
                when(state) {
                    is Resource.Loading -> {
                        _user.value = Resource.loading()
                    }
                    is Resource.Success -> {
                        _user.value = Resource.success(state.data)
                    }
                    is Resource.Failure -> {
                        Timber.e(state.errorMessage)
//                        _user = Resource.failed("")
                    }
                }
            }
        }

}