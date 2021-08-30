package com.ezzy.missingpersontracker.ui.fragments.home

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonViewHolder
import com.ezzy.missingpersontracker.util.applyImage

class HomeViewHolder(
    parent: ViewGroup,
): CommonViewHolder<Pair<MissingPerson, List<Image>>>(parent, R.layout.msp_item) {

    private val coverImage: ImageView = rootView.findViewById(R.id.person_imageview)
    private val name: TextView = rootView.findViewById(R.id.person_name_textview)

    override fun bindItem(item: Pair<MissingPerson, List<Image>>?) {
        coverImage.applyImage(item!!.second[0].image_src!!)
        name.text = item.first.firstName
    }
}