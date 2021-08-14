package com.ezzy.missingpersontracker.ui.fragments.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.Directions
import com.ezzy.missingpersontracker.common.ItemDecorator
import com.ezzy.missingpersontracker.databinding.HomeFragmentBinding
import com.ezzy.missingpersontracker.util.FakerAdapter

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding: HomeFragmentBinding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var mAdapter: FakerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        mAdapter = FakerAdapter(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        )

        context?.let { ctx ->
            binding.missingPpleRecyclerview.apply {
                layoutManager = LinearLayoutManager(ctx)
                adapter = mAdapter
                addItemDecoration(ItemDecorator(Directions.VERTICAL, 5))
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}