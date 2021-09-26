package com.ezzy.missingpersontracker.ui.activities.report_found_person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.interactors.ReportFoundPerson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class FoundPersonViewModel @Inject constructor(
    private val reportFoundPerson: ReportFoundPerson
): ViewModel() {

    private var _foundPersonId = MutableStateFlow<Resource<String>>(Resource.Empty)
    val foundPersonId: StateFlow<Resource<String>> get() = _foundPersonId

    fun reportPersonFound(missingPerson: MissingPerson, address: Address) =
        viewModelScope.launch {
            reportFoundPerson(missingPerson, address).collect { state ->
                when (state) {
                    is Resource.Loading -> _foundPersonId.value = Resource.loading()
                    is Resource.Success -> _foundPersonId.value = Resource.success(state.data)
                    is Resource.Failure -> _foundPersonId.value = Resource.failed(state.errorMessage!!)
                    is Resource.Empty -> _foundPersonId.value = Resource.Empty
                }
            }
        }

}