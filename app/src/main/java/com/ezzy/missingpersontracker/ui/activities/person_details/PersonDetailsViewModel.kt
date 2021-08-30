package com.ezzy.missingpersontracker.ui.activities.person_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.interactors.GetPersonImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val getPersonImages: GetPersonImages
): ViewModel() {

    private var _personImages = MutableStateFlow<Resource<List<Image>>>(Resource.Empty)
    val personImages: StateFlow<Resource<List<Image>>> get() = _personImages

    fun getImages(personId: String) = viewModelScope.launch {
        getPersonImages(personId).collect { resourceState ->
            when(resourceState) {
                is Resource.Loading -> _personImages.value = Resource.loading()
                is Resource.Success -> _personImages.value = Resource.success(resourceState.data)
                is Resource.Failure -> _personImages.value = Resource.failed(resourceState.errorMessage!!)
            }
        }
    }
}