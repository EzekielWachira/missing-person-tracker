package com.ezzy.missingpersontracker.ui.fragments.found_persons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.domain.User
import com.ezzy.core.interactors.GetFoundPeople
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FoundPersonsViewModel @Inject constructor(
    private val getFoundPeople: GetFoundPeople
) : ViewModel() {

    private var _foundPeople =
        MutableStateFlow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>>(Resource.Empty)
    val foundPeople: StateFlow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> get() = _foundPeople

    fun getAllFoundPeople() = viewModelScope.launch {
        getFoundPeople().collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> _foundPeople.value = Resource.loading()
                is Resource.Success -> {
                    Timber.d("THE DATA: ${resourceState.data}")
                    _foundPeople.value = Resource.success(resourceState.data)
                }
                is Resource.Failure -> Timber.e("Could not get found people: ${resourceState.errorMessage}")
                is Resource.Empty -> _foundPeople.value = Resource.Empty
            }
        }
    }

}