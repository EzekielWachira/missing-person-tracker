package com.ezzy.missingpersontracker.ui.fragments.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Contact
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.interactors.AddMissingPerson
import com.ezzy.core.interactors.GetAuthenticatedUserID
import com.ezzy.missingpersontracker.data.model.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class ReportMissingPersonViewModel @Inject constructor(
    private val addMissingPerson: AddMissingPerson,
    private val getAuthenticatedUserID: GetAuthenticatedUserID
) : ViewModel() {

    private val _addMissingPersonState = MutableStateFlow<Resource<String?>>(Resource.Empty)
    val addMissingPersonState: StateFlow<Resource<String?>> get() = _addMissingPersonState
    private var _userId = MutableStateFlow<Resource<String>>(Resource.Empty)
    val userId: StateFlow<Resource<String>> get() = _userId

    private val _readPermissionStatus: MutableLiveData<Boolean> = MutableLiveData()
    val readPermissionStatus: LiveData<Boolean> get() = _readPermissionStatus
    private var _personImages = MutableLiveData<List<ImageItem>>()
    val personImages: LiveData<List<ImageItem>> get() = _personImages
    private var _personContacts = MutableLiveData<List<Contact>>()
    val personContacts: LiveData<List<Contact>> get() = _personContacts
    private var _personAddress = MutableLiveData<Address>()
    val personAddress: LiveData<Address> get() = _personAddress
    private var _missingPerson = MutableLiveData<MissingPerson>()
    val missingPerson: LiveData<MissingPerson> get() = _missingPerson
    private val contactList = mutableListOf<Contact>()
    private var _isPersonSaved = MutableLiveData<Boolean>()
    val isPersonSaved: LiveData<Boolean> get() = _isPersonSaved

    fun updatePermission(value: Boolean) {
        _readPermissionStatus.value = value
    }

    fun addPersonImages(imageList: List<ImageItem>) = _personImages.postValue(imageList)

    fun addAddress(address: Address) {
        _personAddress.postValue(address)
    }

    fun addMissingPersonDetails(missingPerson: MissingPerson) {
        _missingPerson.postValue(missingPerson)
    }

    fun addPersonContacts(contact: Contact) {
        contactList.add(contact)
        _personContacts.postValue(contactList)
    }

    fun saveMissingPerson(
        missingPerson: MissingPerson,
        address: Address,
        contactList: List<Contact>,
        missingPersonImages: List<URI>,
        fileNames: List<String>
    ) = viewModelScope.launch {
        addMissingPerson(
            missingPerson,
            address,
            contactList,
            missingPersonImages,
            fileNames
        ).collect { state ->
            when (state) {
                is Resource.Loading -> {
                    _addMissingPersonState.value = Resource.loading()
                }
                is Resource.Success -> {
                    _addMissingPersonState.value = Resource.success(state.data)
                }
                is Resource.Failure -> {
                    _addMissingPersonState.value = Resource.failed("Error while saving user")
                }
            }
        }
    }

    fun getAuthUserId(email: String?, phone: String?) = viewModelScope.launch {
        getAuthenticatedUserID(email, phone).collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> _userId.value = Resource.loading()
                is Resource.Success -> _userId.value = Resource.success(resourceState.data)
                is Resource.Failure -> _userId.value = Resource.failed(resourceState.errorMessage!!)
            }
        }
    }


}