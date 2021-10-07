package com.ezzy.missingpersontracker.ui.fragments.chats

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ezzy.core.domain.Chat
import com.ezzy.core.domain.ChatMessage
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonViewHolder

class ChatMessageViewHolder(
    parent: ViewGroup,
    private val context: Context
): CommonViewHolder<ChatMessage>(parent, R.layout.chat_item) {

    private val message: TextView = rootView.findViewById(R.id.chatMessage)

    override fun bindItem(item: ChatMessage?) {
        message.text = item?.message
    }
}