package com.exam.fundanavapii.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exam.fundanavapii.databinding.ListItemEventBinding
import com.exam.fundanavapii.response.Event

class ActiveEventsAdapter(private val onClick: (Event) -> Unit) : ListAdapter<Event, ActiveEventsAdapter.EEEventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EEEventViewHolder {
        val binding = ListItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EEEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EEEventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    inner class EEEventViewHolder(private val binding: ListItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.apply {
                tvTitle.text = event.name
                tvOwner.text = event.ownerName
                val imageUrl = event.mediaCover ?: event.imageLogo
                Glide.with(root.context)
                    .load(imageUrl)
                    .into(tvBanner)

                root.setOnClickListener {
                    onClick(event)
                }
            }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}
