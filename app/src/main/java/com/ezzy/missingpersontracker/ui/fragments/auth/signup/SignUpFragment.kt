package com.ezzy.missingpersontracker.ui.fragments.auth.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ezzy.core.data.resource.Resource
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.SignUpFragmentBinding
import com.ezzy.missingpersontracker.ui.fragments.auth.AuthViewModel
import com.ezzy.missingpersontracker.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding: SignUpFragmentBinding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)

        setUpUI()
        subscribeToUI()

        return binding.root
    }

    private fun setUpUI() {
        binding.signInTxt.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.loginButton.setOnClickListener {
            val userEmail = binding.email.text.toString()
            val password = binding.password.text.toString()

//            authViewModel.register(userEmail, password).observe(viewLifecycleOwner) {
//                when(it) {
//                    Resource.Loading -> {
//                        binding.progressIndicator.apply {
//                            show()
//                            isIndeterminate = true
//                        }
//                    }
//                    Resource.Success(it) -> {
//                        binding.progressIndicator.apply {
//                            hide()
//                            isIndeterminate = true
//                        }
//                    }
//                }
//            }
        }

    }

    private fun subscribeToUI() {
//        authViewModel.isRegistrationSuccess.observe(viewLifecycleOwner) {
//            isRegisterSuccess ->
//            if (isRegisterSuccess!!) {
//                this.showToast("User registered")
//                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
//            }
//            else this.showToast("An error occurred while registering user")
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}