package com.ezzy.missingpersontracker.ui.activities.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.domain.User
import com.ezzy.core.interactors.SearchMissingPerson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMissingPeople: SearchMissingPerson
) : ViewModel() {

    private var _missingPeople =
        MutableStateFlow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>>(Resource.Empty)
    val missingPeople: StateFlow<Resource<List<Pair<Pair<MissingPerson, List<Image>>, User>>>> get() =
        _missingPeople

    fun searchMissingPerson(name: String) = viewModelScope.launch {
        searchMissingPeople(name).collect { resourceState->
            when(resourceState) {
                is Resource.Loading -> _missingPeople.value = Resource.loading()
                is Resource.Success -> {
                    _missingPeople.value = Resource.success(resourceState.data)
                }
                is Resource.Failure -> _missingPeople.value = Resource.failed(resourceState.errorMessage!!)
                is Resource.Empty -> _missingPeople.value = Resource.Empty
            }
        }
    }

}