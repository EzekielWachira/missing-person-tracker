package com.ezzy.missingpersontracker.ui.fragments.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isEmpty
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ezzy.core.domain.MissingPerson
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.FragmentPersonContactsBinding
import com.ezzy.missingpersontracker.databinding.FragmentPersonDetailsBinding
import com.ezzy.missingpersontracker.util.isEmpty
import com.ezzy.missingpersontracker.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDetailsFragment : Fragment() {

    private var _binding: FragmentPersonDetailsBinding? = null
    private val binding: FragmentPersonDetailsBinding get() = _binding!!

    private val viewModel: ReportMissingPersonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonDetailsBinding.inflate(inflater, container, false)
        setUpUI()
        return binding.root
    }

    private fun setUpUI() {
        binding.btnNext.setOnClickListener {

            if (binding.firstname.isEmpty() || binding.lastname.isEmpty() || binding.middlename.isEmpty()
                || binding.age.isEmpty() || binding.color.isEmpty() || binding.gender.isEmpty()
                || binding.personStatus.isEmpty() || binding.description.isEmpty()
            ) {
                showToast("Please fill out all the fields")
            } else {
                val missingPerson = MissingPerson(
                    binding.firstname.text.toString(),
                    binding.middlename.text.toString(),
                    binding.lastname.text.toString(),
                    binding.color.text.toString(),
                    binding.personStatus.text.toString(),
                    binding.age.text.toString(),
                    binding.gender.text.toString(),
                    170,
                    67,
                    binding.description.text.toString()
                )
                viewModel.addMissingPersonDetails(missingPerson)
                findNavController().navigate(R.id.action_personDetailsFragment_to_addressFragment)
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