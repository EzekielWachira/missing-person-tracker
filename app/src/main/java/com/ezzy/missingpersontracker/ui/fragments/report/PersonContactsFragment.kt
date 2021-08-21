package com.ezzy.missingpersontracker.ui.fragments.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezzy.core.domain.Contact
import com.ezzy.missingpersontracker.common.CommonAdapter
import com.ezzy.missingpersontracker.common.Directions
import com.ezzy.missingpersontracker.common.ItemDecorator
import com.ezzy.missingpersontracker.databinding.FragmentPersonContactsBinding
import com.ezzy.missingpersontracker.ui.adapter.ContactViewHolder
import com.ezzy.missingpersontracker.ui.dialogs.AddContactsDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PersonContactsFragment : Fragment() {

    private var _binding: FragmentPersonContactsBinding? = null
    private val binding: FragmentPersonContactsBinding get() = _binding!!

    private val viewModel: ReportMissingPersonViewModel by activityViewModels()
    private lateinit var mAdapter: CommonAdapter<Contact>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonContactsBinding.inflate(inflater, container, false)

        setUpUI()
        setUpRecyclerView()
        subscribeToUI()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.personImages.observe(viewLifecycleOwner) { images ->
            images.forEach { Timber.d("IMAGE -> $it") }
        }
    }

    private fun setUpRecyclerView() {
        mAdapter = CommonAdapter { ContactViewHolder(it) }

        binding.contactRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            addItemDecoration(ItemDecorator(Directions.VERTICAL, 5))
        }
    }

    private fun setUpUI () {
        binding.addContactBtn.setOnClickListener {
            AddContactsDialog().show(requireActivity().supportFragmentManager, "ADD_CONTACT")
        }
    }

    private fun subscribeToUI() {
       viewModel.personContacts.observe(viewLifecycleOwner) {
           mAdapter.differ.submitList(it)
       }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}