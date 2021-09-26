package com.ezzy.missingpersontracker.ui.fragments.chats

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ezzy.core.domain.Chat
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonViewHolder

class ChatViewHolder(
    parent: ViewGroup,
    private val context: Context
): CommonViewHolder<Chat>(parent, R.layout.chat_list_item) {

    private val personImage: ImageView = rootView.findViewById(R.id.circleImageView)
    private val sender: TextView = rootView.findViewById(R.id.sender)
    private val message: TextView = rootView.findViewById(R.id.message)

    override fun bindItem(item: Chat?) {

    }
}