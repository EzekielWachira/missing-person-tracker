package com.ezzy.missingpersontracker.ui.fragments.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ezzy.core.domain.Contact
import com.ezzy.missingpersontracker.data.model.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportMissingPersonViewModel @Inject constructor() : ViewModel() {

    private val _readPermissionStatus: MutableLiveData<Boolean> = MutableLiveData()
    val readPermissionStatus: LiveData<Boolean> get() = _readPermissionStatus
    private var _personImages = MutableLiveData<List<ImageItem>>()
    val personImages: LiveData<List<ImageItem>> get() = _personImages
    private var _personContacts = MutableLiveData<List<Contact>>()
    val personContacts: LiveData<List<Contact>> get() = _personContacts

    private val contactList = mutableListOf<Contact>()


    fun updatePermission(value: Boolean) {
        _readPermissionStatus.value = value
    }

    fun addPersonImages(imageList: List<ImageItem>) = _personImages.postValue(imageList)

    fun addPersonContacts(contact: Contact) {
//        val contactList = mutableListOf<Contact>()
//        contactList.add(contact)
        contactList.add(contact)
        _personContacts.postValue(contactList)
    }


}