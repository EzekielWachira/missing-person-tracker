package com.ezzy.missingpersontracker.ui.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPerson
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonViewHolder
import com.ezzy.missingpersontracker.util.applyImage
import com.ezzy.missingpersontracker.util.formatTimeToDate
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class HomeViewHolder(
    parent: ViewGroup,
    val  context: Context
) : CommonViewHolder<Pair<Pair<MissingPerson, List<Image>>, User>>(parent, R.layout.msp_item) {

    private val coverImage: ImageView = rootView.findViewById(R.id.person_imageview)
    private val name: TextView = rootView.findViewById(R.id.person_name_textview)
    private val chipGroup: ChipGroup = rootView.findViewById(R.id.person_properties_chip_grp)
    private val imageCount: TextView = rootView.findViewById(R.id.images_count)
    private val reporter: TextView = rootView.findViewById(R.id.reporter_text_view)
    private val location: TextView = rootView.findViewById(R.id.locationTextView)
    private val postDate: TextView = rootView.findViewById(R.id.post_date)


    override fun bindItem(item: Pair<Pair<MissingPerson, List<Image>>, User>?) {
        if (item?.first?.second!!.isNotEmpty()) {
            coverImage.applyImage(item.first.second[0].image_src!!)
            imageCount.text = "+${item.first.second.size.minus(1)} more images"
        }
        name.text = item.first.first.firstName
        reporter.text = "Reported by: ${item.second.firstName} ${item.second.lastName}"
        item.first.first.reportTime?.let {
            postDate.text = it.formatTimeToDate()
        }
//        location.text = "%${}"

        val ageChip = (LayoutInflater.from(rootView.context)
            .inflate(R.layout.chip_item, null, false) as Chip).apply {
            text = "Age ${item.first.first.age}"
            chipIcon = ContextCompat.getDrawable(context,R.drawable.ic_calendar_account_outline)
        }

        val genderChip = (LayoutInflater.from(rootView.context)
            .inflate(R.layout.chip_item, null, false) as Chip).apply {
            text = "Gender: ${item.first.first.gender}"
            chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_gender)
        }

        val heightChip = (LayoutInflater.from(rootView.context)
            .inflate(R.layout.chip_item, null, false) as Chip).apply {
            text = "Height: ${item.first.first.height}"
            chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_height)
        }

        val weightChip = (LayoutInflater.from(rootView.context)
            .inflate(R.layout.chip_item, null, false) as Chip).apply {
            text = "Weight: ${item.first.first.weight}"
            chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_dumbbell)
        }

        val colorChip = (LayoutInflater.from(rootView.context)
            .inflate(R.layout.chip_item, null, false) as Chip).apply {
            text = "Color: ${item.first.first.color}"
            chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_color_lens)
        }

        chipGroup.addView(ageChip)
        chipGroup.addView(genderChip)
        chipGroup.addView(heightChip)
        chipGroup.addView(weightChip)
        chipGroup.addView(colorChip)

//        val genderChip = chip.apply {
//            text = "Age ${item.first.gender}"
//            chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_gender)
//        }

    }
}