package com.ezzy.missingpersontracker.ui.fragments.auth.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.LoginFragmentBinding
import com.ezzy.missingpersontracker.ui.activities.MainActivity
import com.ezzy.missingpersontracker.ui.fragments.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding: LoginFragmentBinding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        setUpUI()
        return binding.root
    }

    private fun setUpUI() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }

        binding.registerTxt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}