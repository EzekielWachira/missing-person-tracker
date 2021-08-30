package com.ezzy.missingpersontracker.ui.activities.person_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.missingpersontracker.databinding.ActivityPersonDetailsBinding
import com.ezzy.missingpersontracker.ui.activities.report_found_person.ReportFoundPersonActivity
import com.ezzy.missingpersontracker.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PersonDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonDetailsBinding

    private val viewModel: PersonDetailsViewModel by viewModels()

    private var missingPerson: MissingPerson? = null
    private var images: List<Image>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Person details"
            setDisplayHomeAsUpEnabled(true)
        }

        if (intent.hasExtra("missingPerson") && intent.hasExtra("images")) {
            missingPerson = intent.getSerializableExtra("missingPerson") as MissingPerson
//            images = intent.getSerializableExtra("images") as List<Image>
        }

        viewModel.getImages(missin)

        setUpUI()
    }

    private fun setUpUI() {

        with(binding) {

            personName.text = images?.size.toString()

            reportFoundFab.setOnClickListener {
                startActivity(
                    Intent(
                        this@PersonDetailsActivity, ReportFoundPersonActivity::class.java
                    )
                )
            }
        }
    }

    private fun initListeners() {
        lifecycleScope.launchWhenCreated {
            viewModel.personImages.collect { state ->
                when(state) {
                    is Resource.Loading -> showToast("Loading images")
                    is Resource.Success -> images = state.data
                    is Resource.Failure -> showToast("Error ${state.errorMessage}")
                }
            }
        }
    }
}