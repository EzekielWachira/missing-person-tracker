package com.ezzy.missingpersontracker.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.common.CommonAdapter
import com.ezzy.missingpersontracker.common.Directions
import com.ezzy.missingpersontracker.common.ItemDecorator
import com.ezzy.missingpersontracker.databinding.HomeFragmentBinding
import com.ezzy.missingpersontracker.ui.activities.person_details.PersonDetailsActivity
import com.ezzy.missingpersontracker.util.FakerAdapter
import com.ezzy.missingpersontracker.util.gone
import com.ezzy.missingpersontracker.util.showToast
import com.ezzy.missingpersontracker.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding: HomeFragmentBinding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    //    private lateinit var mAdapter: FakerAdapter
    private lateinit var mAdapter: CommonAdapter<Pair<Pair<MissingPerson, List<Image>>, User>>

    private var missingPersons: List<Pair<Pair<MissingPerson, List<Image>>, User>>? = null
    private var missingPersonsImages: List<Image>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        homeViewModel.getAllMissingPeople()
        setUpRecyclerView()
        subscribeToUI()

        return binding.root
    }

    private fun setUpRecyclerView() {
        mAdapter = CommonAdapter {
            HomeViewHolder(it, requireContext())
        }

        binding.missingPpleRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            addItemDecoration(ItemDecorator(Directions.VERTICAL, 5))
        }

//        mAdapter.differ.submitList(missingPersons)
    }

    private fun subscribeToUI() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.missingPeople.collect { resourceState ->
                when (resourceState) {
                    is Resource.Loading -> {
                        binding.spinKit.visible()
                        binding.layoutNoData.gone()
                    }
                    is Resource.Success -> {
                        binding.spinKit.gone()
                        binding.layoutNoData.gone()
                        mAdapter.differ.submitList(resourceState.data)
                    }
                    is Resource.Failure -> {
                        binding.spinKit.gone()
                        binding.layoutNoData.gone()
                        showToast(resourceState.errorMessage!!)
                    }
                    is Resource.Empty -> {
                        binding.layoutNoData.visible()
                    }
                }
            }

//            homeViewModel.personImages.collect { resourceState ->
//                when(resourceState) {
//                    is Resource.Loading -> binding.spinKit.visible()
//                    is Resource.Success -> {
//                        binding.spinKit.gone()
//                        missingPersonsImages = resourceState.data
//                    }
//                    is Resource.Failure -> {
//                        binding.spinKit.gone()
//                        showToast(resourceState.errorMessage!!)
//                    }
//                }
//            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter.setOnClickListener {
            startActivity(Intent(requireContext(), PersonDetailsActivity::class.java).apply {
                putExtra("missingPerson", it?.first?.first)
                putExtra("images", it?.first?.second?.toTypedArray())
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
