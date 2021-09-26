package com.ezzy.missingpersontracker.ui.activities.report_found_person

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Address
import com.ezzy.core.domain.MissingPerson
import com.ezzy.missingpersontracker.databinding.ActivityReportFoundPersonBinding
import com.ezzy.missingpersontracker.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ReportFoundPersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportFoundPersonBinding
    private lateinit var progressDialog: SweetAlertDialog
    private lateinit var successDialog: SweetAlertDialog
    private lateinit var errorDialog: SweetAlertDialog

    private val viewModel: FoundPersonViewModel by viewModels()

    private var missingPerson: MissingPerson? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportFoundPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Report Found Person"
            setDisplayHomeAsUpEnabled(true)
        }

        if (intent.hasExtra("missing_person")) {
            missingPerson = intent.getSerializableExtra("missing_person") as MissingPerson
        }

        successDialog = showSuccessDialog("Person reported successdully", "Person reported as found")
        errorDialog = showErrorDialog("Error", "An error occurred while reporting")
        progressDialog = showLoadingDialog("Reporting person as found")

        setUpUI()
        subscribeToUI()
    }

    private fun subscribeToUI() {
        lifecycleScope.launchWhenCreated {
            viewModel.foundPersonId.collect { state->
                when(state) {
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
            btnReport.setOnClickListener {
                if (country.isEmpty() || city.isEmpty() || state.isEmpty() || town.isEmpty()) {
                    showToast("Please fill out all the fields")
                } else {
                    val address = Address(
                        country.takeText(),
                        city.takeText(),
                        state.takeText(),
                        town.takeText()
                    )

                    missingPerson?.let { msp ->
                        viewModel.reportPersonFound(msp, address)
                    }

                }
            }
        }
    }
}