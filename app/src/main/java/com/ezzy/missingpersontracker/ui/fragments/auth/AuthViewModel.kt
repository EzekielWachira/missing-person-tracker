package com.ezzy.missingpersontracker.ui.fragments.auth

import androidx.lifecycle.*
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.interactors.LoginUser
import com.ezzy.core.interactors.RegisterUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val registerUser: RegisterUser,
    val loginUser: LoginUser
): ViewModel() {

    private var _isRegistrationSuccess = MutableStateFlow<Resource<Boolean?>>(Resource.Empty)
    val isRegistrationSuccess: StateFlow<Resource<Boolean?>> get() = _isRegistrationSuccess
    private var _isLoginSuccess = MutableLiveData<Boolean?>()
    val isLoginSuccess: LiveData<Boolean?> get() = _isLoginSuccess

    fun register(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(registerUser(email, password))
    }


    fun login(email: String, password: String) =
        viewModelScope.launch {
            _isLoginSuccess.postValue(loginUser(email, password))
        }

}