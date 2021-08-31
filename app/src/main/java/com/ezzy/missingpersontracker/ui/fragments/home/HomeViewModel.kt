package com.ezzy.missingpersontracker.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.interactors.GetMissingPeople
import com.ezzy.core.interactors.GetPersonImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMissingPeople: GetMissingPeople,
    private val getPersonImages: GetPersonImages
) : ViewModel() {

    private var _missingPeople = MutableStateFlow<Resource<List<Pair<MissingPerson, List<Image>>>>>(Resource.Empty)
    private val _personImages = MutableStateFlow<Resource<List<Image>>>(Resource.Empty)
    val missingPeople: StateFlow<Resource<List<Pair<MissingPerson, List<Image>>>>> get() = _missingPeople
    val personImages: StateFlow<Resource<List<Image>>> get() = _personImages

    fun getAllMissingPeople() = viewModelScope.launch {
        getMissingPeople().collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> _missingPeople.value = Resource.loading()
                is Resource.Success -> {
                    Timber.d("THE DATA: ${resourceState.data}")
                    _missingPeople.value = Resource.success(resourceState.data)
                }
                is Resource.Failure -> Timber.e("Could not get missing people: ${resourceState.errorMessage}")
                is Resource.Empty -> Timber.e("Could not get missing people: EMPTY!!")
            }
        }
    }

//    fun getPersonAllImages() = viewModelScope.launch {
//        getPersonImages().collect { resourceState ->
//            when (resourceState) {
//                is Resource.Loading -> _personImages.value = Resource.loading()
//                is Resource.Success -> {
//                    _personImages.value = Resource.success(resourceState.data)
//                }
//                is Resource.Failure -> Timber.e("Could not get missing person images: ${resourceState.errorMessage}")
//                is Resource.Empty -> Timber.e("Could not get missing person images: EMPTY!!")
//            }
//        }
//    }
}