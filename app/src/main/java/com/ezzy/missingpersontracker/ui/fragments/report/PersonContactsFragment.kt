package com.ezzy.missingpersontracker.ui.fragments.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.FragmentPersonContactsBinding

class PersonContactsFragment : Fragment() {

    private var _binding: FragmentPersonContactsBinding? = null
    private val binding: FragmentPersonContactsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}