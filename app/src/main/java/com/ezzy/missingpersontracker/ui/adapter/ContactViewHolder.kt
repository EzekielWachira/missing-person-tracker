package com.ezzy.missingpersontracker.ui.adapter

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ezzy.core.domain.Contact
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonViewHolder

class ContactViewHolder(
    parent: ViewGroup
): CommonViewHolder<Contact>(parent, R.layout.contact_item) {

    private val contactImage: ImageView = rootView.findViewById(R.id.iconImage)
    private val contactName: TextView = rootView.findViewById(R.id.contactNameTextView)
    private val contactMain: TextView = rootView.findViewById(R.id.contactMainTextView)

    override fun bindItem(item: Contact?) {
        contactName.text = item?.contactName
        if (item?.contactNumber != null) {
            contactMain.text = item.contactNumber
            contactImage.setImageResource(R.drawable.ic_phone)
        } else if (item?.contactEmail != null) {
            contactMain.text = item.contactEmail
            contactImage.setImageResource(R.drawable.ic_email_outline)
        }
    }
}