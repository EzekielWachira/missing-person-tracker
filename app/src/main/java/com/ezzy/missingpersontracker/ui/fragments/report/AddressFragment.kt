package com.ezzy.missingpersontracker.ui.fragments.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ezzy.core.domain.Address
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.FragmentAddressBinding
import com.ezzy.missingpersontracker.util.isEmpty
import com.ezzy.missingpersontracker.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding: FragmentAddressBinding get() = _binding!!

    private val viewModel: ReportMissingPersonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)

        setUpUI()

        return binding.root
    }

    private fun setUpUI() {
        binding.btnNext.setOnClickListener {

            if (binding.country.isEmpty() ||
                binding.city.isEmpty() ||
                binding.state.isEmpty() ||
                binding.town.isEmpty()
            ) {
                showToast("Please fill out address fields")
            } else {
                val address = Address(
                    binding.country.text.toString(),
                    binding.city.text.toString(),
                    binding.state.text.toString(),
                    binding.town.text.toString()
                )
                viewModel.addAddress(address)
                findNavController().navigate(
                    R.id.action_addressFragment_to_personImagesFragment2
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}