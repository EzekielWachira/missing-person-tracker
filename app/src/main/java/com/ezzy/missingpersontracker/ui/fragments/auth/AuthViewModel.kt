package com.ezzy.missingpersontracker.ui.fragments.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.interactors.LoginUser
import com.ezzy.core.interactors.RegisterUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val registerUser: RegisterUser,
    val loginUser: LoginUser
): ViewModel() {

    private var _isRegistrationSuccess = MutableLiveData<Boolean?>()
    val isRegistrationSuccess: LiveData<Boolean?> get() = _isRegistrationSuccess
    private var _isLoginSuccess = MutableLiveData<Boolean?>()
    val isLoginSuccess: LiveData<Boolean?> get() = _isLoginSuccess

    fun register(email: String, password: String) =
        viewModelScope.launch {
            _isRegistrationSuccess.postValue(registerUser(email, password))
        }

    fun login(email: String, password: String) =
        viewModelScope.launch {
            _isLoginSuccess.postValue(loginUser(email, password))
        }

}