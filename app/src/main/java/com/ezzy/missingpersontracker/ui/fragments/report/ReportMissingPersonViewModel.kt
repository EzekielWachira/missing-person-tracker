package com.ezzy.missingpersontracker.ui.fragments.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Contact
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.interactors.AddMissingPerson
import com.ezzy.missingpersontracker.data.model.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class ReportMissingPersonViewModel @Inject constructor(
    private val addMissingPerson: AddMissingPerson
) : ViewModel() {

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
        missingPersonImages: List<URI>
    ) = viewModelScope.launch {
        val personSavedStatus =
            addMissingPerson(missingPerson, address, contactList, missingPersonImages)
        _isPersonSaved.postValue(personSavedStatus)
    }

}