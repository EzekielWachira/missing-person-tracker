package com.ezzy.missingpersontracker.ui.fragments.chats

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ezzy.core.domain.Chat
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonViewHolder

class ChatListViewHolder(
    parent: ViewGroup,
    private val context: Context
): CommonViewHolder<User>(parent, R.layout.i_chat_item) {

    private val userImage: ImageView = rootView.findViewById(R.id.user_image)
    private val senderEmail: TextView = rootView.findViewById(R.id.person_email)
    private val emailOrPhone: TextView = rootView.findViewById(R.id.emailOrPhone)

    override fun bindItem(item: User?) {
//        item?.email?.let {
//            senderEmail.text = it
//        }
        senderEmail.text = item?.firstName + " " + item?.lastName
        if (item?.email != null) emailOrPhone.text = item.email
        else if (item?.phoneNumber != null) emailOrPhone.text = item.phoneNumber
    }
}