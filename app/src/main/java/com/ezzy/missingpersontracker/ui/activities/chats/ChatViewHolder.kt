package com.ezzy.missingpersontracker.ui.activities.chats

import android.view.ViewGroup
import android.widget.TextView
import com.ezzy.core.domain.ChatMessage
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonViewHolder

class ChatViewHolder(
    parent: ViewGroup
): CommonViewHolder<ChatMessage>(parent, R.layout.chat_item) {

    private val chatMessage: TextView = rootView.findViewById(R.id.chatMessage)

    override fun bindItem(item: ChatMessage?) {
        chatMessage.text = item?.message
    }
}