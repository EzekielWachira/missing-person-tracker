package com.ezzy.missingpersontracker.ui.fragments.auth.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.SignUpFragmentBinding

class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding: SignUpFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)

        setUpUI()

        return binding.root
    }


    private fun setUpUI() {
        binding.signInTxt.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}