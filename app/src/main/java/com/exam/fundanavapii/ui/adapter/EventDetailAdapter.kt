package com.exam.fundanavapii.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exam.fundanavapii.R

class EventDetailAdapter(private val eventDetails: List<Pair<String, CharSequence?>>) : RecyclerView.Adapter<EventDetailAdapter.EventDetailViewHolder>() {

    inner class EventDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val detailNameTextView: TextView = itemView.findViewById(R.id.tv_detail_name)
        private val detailValueTextView: TextView = itemView.findViewById(R.id.tv_detail_value)

        fun bind(detail: Pair<String, CharSequence?>) {
            detailNameTextView.text = detail.first
            detailValueTextView.text = detail.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_detail, parent, false)
        return EventDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventDetailViewHolder, position: Int) {
        holder.bind(eventDetails[position])
    }

    override fun getItemCount(): Int = eventDetails.size
}
