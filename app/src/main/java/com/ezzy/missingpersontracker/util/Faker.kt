package com.ezzy.missingpersontracker.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezzy.missingpersontracker.R

class FakerAdapter(private val fakeList: List<Int>): RecyclerView.Adapter<FakerAdapter.FakerViewHolder>() {

    inner class FakerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FakerViewHolder {
        return FakerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.msp_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FakerViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return fakeList.size
    }
}