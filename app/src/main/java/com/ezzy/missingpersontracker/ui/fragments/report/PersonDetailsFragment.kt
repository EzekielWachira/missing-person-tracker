package com.ezzy.missingpersontracker.ui.fragments.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isEmpty
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.MissingPerson
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.FragmentPersonContactsBinding
import com.ezzy.missingpersontracker.databinding.FragmentPersonDetailsBinding
import com.ezzy.missingpersontracker.util.isEmpty
import com.ezzy.missingpersontracker.util.showToast
import com.ezzy.missingpersontracker.util.takeText
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PersonDetailsFragment : Fragment() {

    private var _binding: FragmentPersonDetailsBinding? = null
    private val binding: FragmentPersonDetailsBinding get() = _binding!!

    private val viewModel: ReportMissingPersonViewModel by activityViewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonDetailsBinding.inflate(inflater, container, false)
        initializeListeners()
        setUpUI()
        subscribeToUI()
        return binding.root
    }

    private fun initializeListeners() {
        with(firebaseAuth.currentUser) {
            when {
                this?.email?.isNotEmpty() == true -> {
                    viewModel.getAuthUserId(this.email, null)
                }
                this?.phoneNumber?.isNotEmpty() == true -> {
                    viewModel.getAuthUserId(null, this.phoneNumber)
                }
                else -> return@with
            }
        }
    }

    private fun subscribeToUI() {
        lifecycleScope.launchWhenCreated {
            viewModel.userId.collect { resourceState ->
                when (resourceState) {
                    is Resource.Loading -> {
                        Timber.i("Loading user id...")
                    }
                    is Resource.Success -> {
                        Timber.d("USER ID: ${resourceState.data}")
                        userId = resourceState.data
                    }
                    is Resource.Failure -> {
                        Timber.d("Error loading user id: ${resourceState.errorMessage}")
                    }
                    is Resource.Empty -> {
                        Timber.d("Error loading user id")
                    }
                }
            }
        }
    }

    private fun setUpUI() {
        with(binding) {
            binding.btnNext.setOnClickListener {

                if (firstname.isEmpty() || lastname.isEmpty() || middlename.isEmpty()
                    || age.isEmpty() || color.isEmpty() || gender.isEmpty()
                    || personStatus.isEmpty() || description.isEmpty()
                ) {
                    showToast("Please fill out all the fields")
                } else {
                    val missingPerson = MissingPerson(
                        firstname.takeText(),
                        middlename.takeText(),
                        lastname.takeText(),
                        color.takeText(),
                        personStatus.text.toString(),
                        age.takeText(),
                        gender.text.toString(),
                        170f,
                        60f,
                        description.takeText(),
                        userId
                    )
                    viewModel.addMissingPersonDetails(missingPerson)
                    findNavController().navigate(R.id.action_personDetailsFragment_to_addressFragment)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val gender = resources.getStringArray(R.array.gender_arr)
        val personStatusArray = resources.getStringArray(R.array.person_type)
        val genderArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, gender)
        val personStatusAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, personStatusArray)
        binding.gender.setAdapter(genderArrayAdapter)
        binding.personStatus.setAdapter(personStatusAdapter)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}