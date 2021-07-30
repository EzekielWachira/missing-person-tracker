package com.ezzy.missingpersontracker.ui.fragments.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }

}