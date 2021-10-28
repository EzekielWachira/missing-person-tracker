package com.ezzy.missingpersontracker.ui.activities.person_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.domain.User
import com.ezzy.core.interactors.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val getPersonImages: GetPersonImages,
    private val getMissingPersonReporter: GetMissingPersonReporter,
    private val getMissingPersonImages: GetMissingPersonImages,
    private val getMissingPersonId: GetMissingPersonId,
    private val getFoundPersonAddress: GetFoundPersonAddress
) : ViewModel() {

    private var _personImages = MutableStateFlow<Resource<List<Image>>>(Resource.Empty)
    private var _reporter = MutableStateFlow<Resource<User>>(Resource.Empty)
    private var _missingPersonId = MutableStateFlow<Resource<String>>(Resource.Empty)
    private var _foundPersonAddress = MutableStateFlow<Resource<Address>>(Resource.Empty)
    val personImages: StateFlow<Resource<List<Image>>> get() = _personImages
    val reporter: StateFlow<Resource<User>> get() = _reporter
    val missingPersonId: StateFlow<Resource<String>> get() = _missingPersonId
    val foundPersonAddress: StateFlow<Resource<Address>> get() = _foundPersonAddress

    fun getImages(missingPerson: MissingPerson) = viewModelScope.launch {
        getMissingPersonImages(missingPerson).collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> _personImages.value = Resource.loading()
                is Resource.Success -> _personImages.value = Resource.success(resourceState.data)
                is Resource.Failure -> _personImages.value =
                    Resource.failed(resourceState.errorMessage!!)
            }
        }
    }

    fun getReporter(userId: String) = viewModelScope.launch {
        getMissingPersonReporter(userId).collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> _reporter.value = Resource.loading()
                is Resource.Success -> _reporter.value = Resource.success(resourceState.data)
                is Resource.Failure -> _reporter.value =
                    Resource.failed(resourceState.errorMessage!!)
            }
        }
    }

    fun getMissingPersonID(missingPerson: MissingPerson) = viewModelScope.launch {
        getMissingPersonId(missingPerson).collect { state ->
            when (state) {
                is Resource.Loading -> {
                    _missingPersonId.value = Resource.loading()
                    Timber.d("Loading the id...")
                }
                is Resource.Success -> {
                    _missingPersonId.value = Resource.success(state.data)
                    Timber.e("EDATA: ${state.data}")
                }
                is Resource.Failure -> {
                    Timber.e("ERRDATA: ${state.errorMessage}")
                    _missingPersonId.value = Resource.failed(state.errorMessage!!)
                }
            }
        }
    }

    fun getFoundPersonAdd(missingPersonId: String) = viewModelScope.launch {
        getFoundPersonAddress(missingPersonId).collect { state ->
            when (state) {
                is Resource.Loading -> _foundPersonAddress.value = Resource.loading()
                is Resource.Success -> _foundPersonAddress.value = Resource.success(state.data)
                is Resource.Failure -> _foundPersonAddress.value =
                    Resource.failed(state.errorMessage!!)
            }
        }
    }
}