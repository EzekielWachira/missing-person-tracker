package com.ezzy.missingpersontracker.ui.activities.person_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.common.CommonAdapter
import com.ezzy.missingpersontracker.common.Directions
import com.ezzy.missingpersontracker.common.ItemDecorator
import com.ezzy.missingpersontracker.databinding.ActivityPersonDetailsBinding
import com.ezzy.missingpersontracker.ui.activities.report_found_person.ReportFoundPersonActivity
import com.ezzy.missingpersontracker.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class PersonDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonDetailsBinding
    private lateinit var imageAdapter: CommonAdapter<Image>

    private val viewModel: PersonDetailsViewModel by viewModels()

    private var missingPerson: MissingPerson? = null
    private var images: List<Image>? = null
    private var reporter: User? = null

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
            viewModel.getReporter(missingPerson?.reporterId!!)
            viewModel.getImages(missingPerson!!)
//            images = intent.getSerializableExtra("images") as List<Image>
        }

//        viewModel.getImages(missin)

        setUpRecyclerView()
        setUpUI()
        initListeners()
    }

    private fun setUpUI() {

        with(binding) {

            personName.text =
                "${missingPerson?.firstName} ${missingPerson?.middleName} ${missingPerson?.lastName}"
            description.text = missingPerson?.description
            age.text = missingPerson?.age + " Years"
            height.text = missingPerson?.height.toString() + " Metres"
            weight.text = missingPerson?.weight.toString() + " Pounds"
            gender.text = missingPerson?.gender
            color.text = missingPerson?.color


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
                when (state) {
                    is Resource.Loading -> showToast("Loading images")
                    is Resource.Success -> {
                        Timber.e("IMAGE DATA: ${state.data}")
                        imageAdapter.differ.submitList(state.data)
                    }
                    is Resource.Failure -> showToast("Error ${state.errorMessage}")
                }
            }.also {
                viewModel.reporter.collect { state ->
                    when (state) {
                        is Resource.Loading -> showToast("Loading reporter")
                        is Resource.Success -> {
                            with(binding) {
                                reporterName.text =
                                    "${state.data.firstName} ${state.data.lastName} "
                                reporterPhone.text = state.data.phoneNumber
                                reporterEmail.text = state.data.email
                            }
                        }
                        is Resource.Failure -> showToast("Error ${state.errorMessage}")
                    }
                }
            }

        }
    }

    private fun setUpRecyclerView() {
        imageAdapter = CommonAdapter { ImageViewHolder(it) }
        binding.photoRecyclerView.apply {
            layoutManager = GridLayoutManager(this@PersonDetailsActivity,
                2)
            addItemDecoration(ItemDecorator(Directions.VERTICAL, 3))
            addItemDecoration(ItemDecorator(Directions.HORIZONTAL, 3))
            adapter = imageAdapter
        }
    }
}