package com.dicoding.asclepius.view.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.asclepius.data.entity.HistoryEntity
import com.tugas.capstoneproject_historia.R
import com.tugas.capstoneproject_historia.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)

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
            binding.textViewTime.text = historyItem.date
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
