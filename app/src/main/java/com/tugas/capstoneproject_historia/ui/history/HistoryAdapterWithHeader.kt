/*
package com.tugas.capstoneproject_historia.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.utils.DateFormatter
import com.tugas.capstoneproject_historia.R
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity
import com.tugas.capstoneproject_historia.databinding.HistoryDayHeaderBinding
import com.tugas.capstoneproject_historia.databinding.ItemHistoryBinding

class HistoryAdapterWithHeader (private val historyItems: List<HistoryEntity>) :
RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var groupedByDate = HashMap<String, MutableList<HistoryItem>>()
        .apply {
            historyItems.forEach { item ->
                val dateString = (item.date) // Implement formatDate() to format date
                getOrCreateList(dateString).add(HistoryItem(date = dateString, title = item.title, imageUrl = item.imageUri))

            }
        }

    private fun getOrCreateList(dateString: String): MutableList<HistoryItem> {
        return groupedByDate.getOrPut(dateString) { mutableListOf() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val bindingDate = HistoryDayHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        LayoutInflater.from(parent.context)
        return if (viewType == HISTORY_ITEM_TYPE) {
            HistoryItemViewHolder(binding)
        } else {
            DayHeaderViewHolder(bindingDate)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            if (holder is HistoryItemViewHolder) {
                holder.bind(item)
            } else if (holder is DayHeaderViewHolder) {
                holder.bind(item) // Pass date to DayHeaderViewHolder
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val previousItem = getItem(position - 1)
        val currentItem = getItem(position)
        return if (previousItem != null && currentItem!!.date != previousItem.date) {
            DAY_HEADER_TYPE
        } else {
            HISTORY_ITEM_TYPE
        }
    }

    private fun getItem(position: Int): HistoryItem? {
        val flattenedList = groupedByDate.values.flatten()
        return if (position < flattenedList.size) flattenedList[position] else null
    }

    override fun getItemCount(): Int {
        val flattenedList = groupedByDate.values.flatten()
        return flattenedList.size
    }

    companion object {
        const val HISTORY_ITEM_TYPE = 0
        const val DAY_HEADER_TYPE = 1
    }
}

class HistoryItemViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(historyItem: HistoryItem) {
        binding.textViewLabel.text = historyItem.title
        binding.textViewTime.text = DateFormatter.formatDateToHours(historyItem.date)
        Glide.with(itemView.context)
            .load(historyItem.imageUrl)
            .into(binding.profileImage)
    }
}

class DayHeaderViewHolder(val binding: HistoryDayHeaderBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(item: HistoryItem) {
        binding.dataHeader.text = DateFormatter.formatDateToHours(item.date)
    }
}

data class HistoryItem(val date: String, val title: String, val details: String? = null, val imageUrl: String?)

*/
