package com.ezzy.missingpersontracker.ui.fragments.found_persons

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonAdapter
import com.ezzy.missingpersontracker.common.Directions
import com.ezzy.missingpersontracker.common.ItemDecorator
import com.ezzy.missingpersontracker.databinding.FoundPersonsFragmentBinding
import com.ezzy.missingpersontracker.ui.activities.person_details.PersonDetailsActivity
import com.ezzy.missingpersontracker.ui.fragments.home.HomeViewHolder
import com.ezzy.missingpersontracker.util.gone
import com.ezzy.missingpersontracker.util.showToast
import com.ezzy.missingpersontracker.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FoundPersonsFragment : Fragment() {

    private var _binding: FoundPersonsFragmentBinding? = null
    private val binding: FoundPersonsFragmentBinding get() = _binding!!
    private val viewModel: FoundPersonsViewModel by viewModels()

    private lateinit var mAdapter: CommonAdapter<Pair<Pair<MissingPerson, List<Image>>, User>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FoundPersonsFragmentBinding.inflate(inflater, container, false)
        viewModel.getAllFoundPeople()
        setUpRecyclerView()
        subscribeToUI()

        return binding.root
    }

    private fun subscribeToUI() {
        lifecycleScope.launchWhenCreated {
            viewModel.foundPeople.collect { resourceState ->
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
        }
    }

    private fun setUpRecyclerView() {
        mAdapter = CommonAdapter {
            HomeViewHolder(it, requireContext())
        }
        binding.foundPersonsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            addItemDecoration(ItemDecorator(Directions.VERTICAL, 5))
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