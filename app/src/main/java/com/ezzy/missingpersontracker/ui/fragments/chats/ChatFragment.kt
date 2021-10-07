package com.ezzy.missingpersontracker.ui.fragments.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezzy.core.domain.ChatMessage
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonAdapter
import com.ezzy.missingpersontracker.common.Directions
import com.ezzy.missingpersontracker.common.ItemDecorator
import com.ezzy.missingpersontracker.databinding.FragmentChatBinding
import com.ezzy.missingpersontracker.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding get() = _binding!!

    private lateinit var mAdapter: CommonAdapter<ChatMessage>
    private var user: User? = null

    private val args: ChatFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        user = args.user

        showToast("${user!!.firstName}")

        setUpRecyclerView()
        setUpUI()
        return binding.root
    }

    private fun setUpUI() {
        with(binding) {

        }
    }

    private fun setUpRecyclerView() {
        mAdapter = CommonAdapter {
            ChatMessageViewHolder(it, requireContext())
        }

        binding.messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            addItemDecoration(ItemDecorator(Directions.VERTICAL, 5))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}