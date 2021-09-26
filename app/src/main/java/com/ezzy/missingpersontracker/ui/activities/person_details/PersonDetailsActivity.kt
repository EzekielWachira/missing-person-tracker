package com.ezzy.missingpersontracker.ui.activities.person_details

import android.content.Intent
import android.net.Uri
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
import com.ezzy.missingpersontracker.ui.activities.ChatActivity
import com.ezzy.missingpersontracker.ui.activities.report_found_person.ReportFoundPersonActivity
import com.ezzy.missingpersontracker.util.gone
import com.ezzy.missingpersontracker.util.showToast
import com.ezzy.missingpersontracker.util.visible
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
    private var personName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Person details"
            setDisplayHomeAsUpEnabled(true)
        }

        if (intent.hasExtra("missingPerson")) {
            missingPerson = intent.getSerializableExtra("missingPerson") as MissingPerson
            Timber.d("PERSON DETAILS: $missingPerson")
            viewModel.getReporter(missingPerson?.reporterId!!)
            viewModel.getImages(missingPerson!!)
//            images = intent.getSerializableExtra("images") as List<Image>
        }

        if (missingPerson?.foundStatus!!) {
            binding.reportFoundFab.hide()
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
                    ).apply { putExtra("missing_person", missingPerson) }
                )
            }

            messageReporter.setOnClickListener {
                startActivity(Intent(this@PersonDetailsActivity, ChatActivity::class.java))
            }

            reporterPhone.setOnClickListener {
                val callIntent: Intent = Uri.parse("tel: ${reporterPhone.text}").let { number ->
                    Intent(Intent.ACTION_DIAL, number)
                }
                startActivity(callIntent)
            }

            reporterEmail.setOnClickListener {
                val email = reporterEmail.text.toString()
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("ezekielwachira048@gmail.com"))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        missingPerson?.firstName + " " + missingPerson?.lastName
                    )
                    putExtra(Intent.EXTRA_TEXT, "")
                }
                startActivity(emailIntent)
            }
        }
    }

    private fun initListeners() {
        lifecycleScope.launchWhenCreated {

            viewModel.personImages.collect { state ->
                when (state) {
                    is Resource.Loading -> binding.spinKitImages.visible()
                    is Resource.Success -> {
                        binding.spinKitImages.gone()
                        Timber.e("IMAGE DATA: ${state.data}")
                        imageAdapter.differ.submitList(state.data)
                    }
                    is Resource.Failure -> binding.spinKitImages.gone()
                }
            }

        }

        lifecycleScope.launchWhenCreated {
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

    private fun setUpRecyclerView() {
        imageAdapter = CommonAdapter { ImageViewHolder(it) }
        binding.photoRecyclerView.apply {
            layoutManager = GridLayoutManager(
                this@PersonDetailsActivity,
                2
            )
            addItemDecoration(ItemDecorator(Directions.VERTICAL, 3))
            addItemDecoration(ItemDecorator(Directions.HORIZONTAL, 3))
            adapter = imageAdapter
        }
    }
}