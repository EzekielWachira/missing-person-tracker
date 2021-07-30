package com.ezzy.missingpersontracker.ui.fragments.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.FragmentPersonContactsBinding
import com.ezzy.missingpersontracker.databinding.FragmentPersonDetailsBinding

class PersonDetailsFragment : Fragment() {

    private var _binding: FragmentPersonDetailsBinding? = null
    private val binding: FragmentPersonDetailsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonDetailsBinding.inflate(inflater, container, false)
        setUpUI()
        return binding.root
    }

    private fun setUpUI(){
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_personDetailsFragment_to_personImagesFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let { ctx ->
            val gender = resources.getStringArray(R.array.gender_arr)
            val personStatusArray = resources.getStringArray(R.array.person_type)
            val genderArrayAdapter = ArrayAdapter(ctx, R.layout.dropdown_item, gender)
            val personStatusAdapter = ArrayAdapter(ctx, R.layout.dropdown_item, personStatusArray)
            binding.gender.setAdapter(genderArrayAdapter)
            binding.personStatus.setAdapter(personStatusAdapter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }

}