package com.ezzy.missingpersontracker.ui.activities.user_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.Location
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.ActivityUserDetailsBinding
import com.ezzy.missingpersontracker.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var successDialog: SweetAlertDialog
    private lateinit var progressDialog: SweetAlertDialog
    private lateinit var errorDialog: SweetAlertDialog

    private val userViewModel: UserDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        successDialog = showSuccessDialog(
            getString(R.string.success),
            getString(R.string.user_data_saved)
        )

        progressDialog = showLoadingDialog(getString(R.string.saving_data))
        errorDialog = showErrorDialog(
            getString(R.string.error),
            getString(R.string.saving_user_data_error)
        )

        setUpUI()
        subscribeToUI()
    }

    private fun subscribeToUI() {
        lifecycleScope.launchWhenCreated {
            userViewModel.userId.collect { resourceStatus ->
                when (resourceStatus) {
                    is Resource.Loading -> progressDialog.show()
                    is Resource.Success -> {
                        progressDialog.hide()
                        successDialog.show()
                    }
                    is Resource.Failure -> {
                        progressDialog.hide()
                        errorDialog.show()
                    }
                }
            }
        }
    }

    private fun setUpUI() {
        with(binding) {
            btnDone.setOnClickListener {
                val isError = checkForEmptyFields()
                if (!isError) {
                    val user = User(
                        firstname.takeText(),
                        lastName.takeText(),
                        emailAddress.takeText(),
                        userName.takeText(),
                        phone.takeText()
                    )

                    val address = Address(
                        country.takeText(),
                        city.takeText(),
                        state.takeText(),
                        town.takeText()
                    )

                    val location = Location("1234", 34.34f, 23.23f, "Nairobi")

                    userViewModel.saveUserDetails(user, address, location)
                }
            }
        }
    }

    private fun checkForEmptyFields(): Boolean {
        var isError: Boolean = false
        with(binding) {
            when {
                firstname.isEmpty() -> {
                    isError = true
                    firstNameLyt.showError(getString(R.string.empty_field_error))
                }
                lastName.isEmpty() -> {
                    isError = true
                    lastNameLyt.showError(getString(R.string.empty_field_error))
                }
                phone.isEmpty() -> {
                    isError = true
                    phoneLyt.showError(getString(R.string.empty_field_error))
                }
                city.isEmpty() -> {
                    isError = true
                    cityLyt.showError(getString(R.string.empty_field_error))
                }
                country.isEmpty() -> {
                    isError = true
                    countryNameLyt.showError(getString(R.string.empty_field_error))
                }
                state.isEmpty() -> {
                    isError = true
                    stateLyt.showError(getString(R.string.empty_field_error))
                }
                town.isEmpty() -> {
                    isError = true
                    countryNameLyt.showError(getString(R.string.empty_field_error))
                }
                else -> {
                    isError = false
                    firstNameLyt.clearError()
                    lastNameLyt.clearError()
                    phoneLyt.clearError()
                    cityLyt.clearError()
                    countryNameLyt.clearError()
                    stateLyt.clearError()
                    countryNameLyt.clearError()
                }
            }
        }
        return isError
    }

}