package com.exam.fundanavapii.ui.adapter

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exam.fundanavapii.databinding.ListItemEventBinding
import com.exam.fundanavapii.response.Event

class CompletedEventsAdapter(private val onEventClick: (Event) -> Unit) : ListAdapter<Event, CompletedEventsAdapter.EEventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EEventViewHolder {
        val binding = ListItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EEventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onEventClick(event)
        }
    }

    inner class EEventViewHolder(private val binding: ListItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.apply {
                tvTitle.text = event.name
                tvOwner.text = event.ownerName

                val isLandscape = root.context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                val imageUrl = if (isLandscape) event.mediaCover else event.imageLogo

                Glide.with(root.context)
                    .load(imageUrl)
                    .into(tvBanner)
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