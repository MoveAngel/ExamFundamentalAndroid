package com.exam.fundanavapii.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.exam.fundanavapii.R
import com.exam.fundanavapii.ui.adapter.ActiveEventsAdapter
import com.exam.fundanavapii.ui.viewmodel.ActiveEventsViewModel


class ActiveEventsFragment : Fragment() {

    private lateinit var adapter: ActiveEventsAdapter
    private lateinit var rvActiveEvents: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: ActiveEventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_active_events, container, false)
        rvActiveEvents = view.findViewById(R.id.rv_active_evdcdng)
        progressBar = view.findViewById(R.id.progressBar)


        viewModel = ViewModelProvider(this)[ActiveEventsViewModel::class.java]

        adapter = ActiveEventsAdapter { event ->
            val fragment = EventDetailFragment()
            val bundle = Bundle().apply {
                event.id?.let { putInt("EVENT_ID", it) }
            }
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        rvActiveEvents.adapter = adapter

        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.fetchActiveEvents()
        return view
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
