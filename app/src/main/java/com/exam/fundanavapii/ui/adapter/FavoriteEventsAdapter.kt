package com.exam.fundanavapii.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exam.fundanavapii.databinding.ListItemEventBinding
import com.exam.fundanavapii.response.FavoriteEvent

class FavoriteEventsAdapter(
    private val onEventClick: (FavoriteEvent) -> Unit
) : ListAdapter<FavoriteEvent, FavoriteEventsAdapter.FavoriteEventViewHolder>(FavoriteEventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ListItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteEventViewHolder(
        private val binding: ListItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEvent) {
            binding.apply {
                tvTitle.text = event.name
                tvOwner.text = event.ownerName

                val imageUrl = event.mediaCover ?: event.imageLogo
                Glide.with(root.context)
                    .load(imageUrl)
                    .into(tvBanner)

                root.setOnClickListener {
                    onEventClick(event)
                }
            }
        }
    }

    class FavoriteEventDiffCallback : DiffUtil.ItemCallback<FavoriteEvent>() {
        override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
            return oldItem == newItem
        }
    }
}

