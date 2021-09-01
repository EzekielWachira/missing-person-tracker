package com.ezzy.missingpersontracker.ui.activities.person_details

import android.view.ViewGroup
import android.widget.ImageView
import com.ezzy.core.domain.Image
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonViewHolder
import com.ezzy.missingpersontracker.util.applyImage

class ImageViewHolder(
    parent: ViewGroup,
): CommonViewHolder<Image>(parent, R.layout.image_item) {

    private val imageView: ImageView = rootView.findViewById(R.id.personImage)

    override fun bindItem(item: Image?) {
        imageView.applyImage(item?.image_src!!)
    }
}