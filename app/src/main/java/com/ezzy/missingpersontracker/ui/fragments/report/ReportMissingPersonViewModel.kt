package com.ezzy.missingpersontracker.ui.fragments.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ReportMissingPersonViewModel: ViewModel() {
    private val _readPermissionStatus: MutableLiveData<Boolean> = MutableLiveData()
    val readPermissionStatus: LiveData<Boolean> get() = _readPermissionStatus
    fun updatePermission(value: Boolean) {
        _readPermissionStatus.value = value
    }
}