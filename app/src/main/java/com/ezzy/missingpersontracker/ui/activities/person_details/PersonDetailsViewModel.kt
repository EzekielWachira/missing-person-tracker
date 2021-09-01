package com.ezzy.missingpersontracker.ui.activities.person_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.User
import com.ezzy.core.interactors.GetMissingPersonReporter
import com.ezzy.core.interactors.GetPersonImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val getPersonImages: GetPersonImages,
    private val getMissingPersonReporter: GetMissingPersonReporter
): ViewModel() {

    private var _personImages = MutableStateFlow<Resource<List<Image>>>(Resource.Empty)
    private var _reporter = MutableStateFlow<Resource<User>>(Resource.Empty)
    val personImages: StateFlow<Resource<List<Image>>> get() = _personImages
    val reporter: StateFlow<Resource<User>> get() = _reporter

    fun getImages(personId: String) = viewModelScope.launch {
        getPersonImages(personId).collect { resourceState ->
            when(resourceState) {
                is Resource.Loading -> _personImages.value = Resource.loading()
                is Resource.Success -> _personImages.value = Resource.success(resourceState.data)
                is Resource.Failure -> _personImages.value = Resource.failed(resourceState.errorMessage!!)
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
}