package com.ezzy.missingpersontracker.ui.activities.run_face_identification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.interactors.SearchMissingPerson
import com.ezzy.core.interactors.SearchMissingPersonByFirstName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FaceIdentificationViewModel @Inject constructor(
    private val searchMissingPersonByFirstName: SearchMissingPersonByFirstName
): ViewModel() {

    private var _missingPersons = MutableStateFlow<Resource<List<MissingPerson>>>(Resource.Empty)
    val missingPersons : StateFlow<Resource<List<MissingPerson>>> get() = _missingPersons

    fun searchMissingPersonByName(name: String) = viewModelScope.launch {
        searchMissingPersonByFirstName(name).collect { state ->
            when(state) {
                is Resource.Loading -> _missingPersons.value = Resource.loading()
                is Resource.Success ->{
                    _missingPersons.value = Resource.success(state.data)
                }
                is Resource.Failure -> _missingPersons.value = Resource.failed(state.errorMessage!!)
            }
        }
    }

}