package com.tugas.capstoneproject_historia.ui.history

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.utils.DateFormatter
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity
import com.tugas.capstoneproject_historia.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        try {
            val historyPrev = getItem(position - 1)
            if (DateFormatter.formatDateToDate(history.date) == DateFormatter.formatDateToDate(historyPrev.date))
            {
                holder.bind(history)
                holder.binding.tvDateHeader.visibility = View.GONE
                holder.binding.divider.visibility = View.GONE
                Log.d("DateHeaderAdapter", "hehehe")
            } else {
                holder.bind(history)
                holder.binding.tvDateHeader.visibility = View.VISIBLE
                holder.binding.divider.visibility = View.VISIBLE
                Log.d("DateHeaderAdapter", "anjirlah")

            }
        } catch (e: IndexOutOfBoundsException) {
            holder.bind(history)
            holder.binding.tvDateHeader.visibility = View.VISIBLE
            holder.binding.divider.visibility = View.VISIBLE
            Log.d("DateHeaderAdapter", e.message!!)
        }

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(history)
        }
    }


    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(historyItem: HistoryEntity) {
            binding.textViewLabel.text = historyItem.title
            binding.tvDateHeader.text = DateFormatter.formatDateToDate(historyItem.date)
            binding.textViewTime.text = DateFormatter.formatDateToHours(historyItem.date)
            binding.tvConfidenceScore.text = "${historyItem.confidenceScore}%"
            Glide.with(itemView.context)
                .load(historyItem.imageUri)
                .into(binding.profileImage)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: HistoryEntity)
    }
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HistoryEntity> =
            object : DiffUtil.ItemCallback<HistoryEntity>() {
                override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
