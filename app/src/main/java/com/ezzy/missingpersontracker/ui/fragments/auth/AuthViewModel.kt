package com.ezzy.missingpersontracker.ui.fragments.auth

import androidx.lifecycle.*
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.interactors.LoginUser
import com.ezzy.core.interactors.RegisterUser
import com.ezzy.missingpersontracker.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val registerUser: RegisterUser,
    val loginUser: LoginUser,
    val preferencesRepository: PreferencesRepository
): ViewModel() {

    private var _isRegistrationSuccess = MutableStateFlow<Resource<Boolean?>>(Resource.Empty)
    val isRegistrationSuccess: StateFlow<Resource<Boolean?>> get() = _isRegistrationSuccess
    private var _isLoginSuccess = MutableLiveData<Boolean?>()
    val isLoginSuccess: LiveData<Boolean?> get() = _isLoginSuccess

    val loginStatus = liveData {
        preferencesRepository.loginStatusFromDataStore.collect {
            emit(it)
        }
    }

    val walkThroughStatus = liveData {
        preferencesRepository.walkThroughStatusFromDataStore.collect {
            emit(it)
        }
    }

    fun register(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(registerUser(email, password))
    }


    fun login(email: String, password: String) =
        viewModelScope.launch {
            _isLoginSuccess.postValue(loginUser(email, password))
        }

    fun saveUserEmailToDataStore(email: String) = viewModelScope.launch {
        preferencesRepository.saveUserEmailToDataStore(email)
    }

    fun saveLoginStatusToDataStore(isUserLoggedIn: Boolean) = viewModelScope.launch {
        preferencesRepository.saveUserLoginStatus(isUserLoggedIn)
    }


}