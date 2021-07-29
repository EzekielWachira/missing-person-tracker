package com.ezzy.missingpersontracker.ui.fragments.found_persons

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezzy.missingpersontracker.R

class FoundPersonsFragment : Fragment() {

    companion object {
        fun newInstance() = FoundPersonsFragment()
    }

    private lateinit var viewModel: FoundPersonsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.found_persons_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FoundPersonsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}