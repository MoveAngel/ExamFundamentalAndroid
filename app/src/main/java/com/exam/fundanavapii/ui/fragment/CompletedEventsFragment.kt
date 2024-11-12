package com.exam.fundanavapii.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.exam.fundanavapii.R
import com.exam.fundanavapii.ui.adapter.CompletedEventsAdapter
import com.exam.fundanavapii.ui.viewmodel.CompletedEventViewModel

class CompletedEventsFragment : Fragment() {

    private lateinit var adapter: CompletedEventsAdapter
    private lateinit var rvCompletedEvents: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: CompletedEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_completed_events, container, false)
        rvCompletedEvents = view.findViewById(R.id.rv_completed_evdcdng)
        progressBar = view.findViewById(R.id.progressBar)

        viewModel = ViewModelProvider(this)[CompletedEventViewModel::class.java]

        adapter = CompletedEventsAdapter { event ->
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

        rvCompletedEvents.adapter = adapter
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvCompletedEvents.layoutManager = staggeredGridLayoutManager

        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }

        if (viewModel.events.value.isNullOrEmpty()) {
            viewModel.fetchCompletedEvents()
        }

        return view
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
