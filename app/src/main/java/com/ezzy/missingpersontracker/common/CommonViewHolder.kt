package com.ezzy.missingpersontracker.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class CommonViewHolder<in T>(
    parent: ViewGroup,
    layoutResource: Int,
    val rootView: View = LayoutInflater.from(parent.context)
        .inflate(layoutResource, parent, false)
) : RecyclerView.ViewHolder(rootView){
    abstract fun bindItem(item : T?)
}