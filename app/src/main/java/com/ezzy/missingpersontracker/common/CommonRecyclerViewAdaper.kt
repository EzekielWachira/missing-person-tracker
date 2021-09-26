package com.ezzy.missingpersontracker.common

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * This is a common generic adapter which will be reused when we
 * create or add a recyclerview in the application
 * */
class CommonAdapter<T>(
    private val viewHolderFactory : ((parent : ViewGroup) -> CommonViewHolder<T>),
) : RecyclerView.Adapter<CommonViewHolder<T>>() {

    private var onItemClickListener : ((T?) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun setOnClickListener(listener : (T?) -> Unit){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<T> {
        return viewHolderFactory(parent)
    }

    override fun onBindViewHolder(holder: CommonViewHolder<T>, position: Int) {
        val item = differ.currentList[position]
        holder.apply {
            bindItem(item)
            rootView.setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}