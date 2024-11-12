package com.exam.fundanavapii.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.exam.fundanavapii.R
import com.exam.fundanavapii.databinding.ActivityHomeEventsBinding
import com.exam.fundanavapii.response.Event
import com.exam.fundanavapii.ui.adapter.HomeEventsAdapter
import com.exam.fundanavapii.ui.viewmodel.HomeEventsViewModel

class HomeEventsFragment : Fragment() {

    private var _binding: ActivityHomeEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeEventsViewModel
    private lateinit var activeEventsAdapter: HomeEventsAdapter
    private lateinit var completedEventsAdapter: HomeEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityHomeEventsBinding.inflate(inflater, container, false)
        val view = binding.root

        setupViewModel()
        setupAdapters()
        setupRecyclerViews()
        observeViewModel()

        return view
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[HomeEventsViewModel::class.java]
        viewModel.fetchEvents()
    }

    private fun setupAdapters() {
        activeEventsAdapter = HomeEventsAdapter { event -> onEventClick(event) }
        completedEventsAdapter = HomeEventsAdapter { event -> onEventClick(event) }
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewActiveEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewCompletedEvents.layoutManager = LinearLayoutManager(context)

        binding.recyclerViewActiveEvents.adapter = activeEventsAdapter
        binding.recyclerViewCompletedEvents.adapter = completedEventsAdapter
    }

    private fun observeViewModel() {
        viewModel.activeEvents.observe(viewLifecycleOwner) { events ->
            activeEventsAdapter.submitList(events)
        }

        viewModel.completedEvents.observe(viewLifecycleOwner) { events ->
            completedEventsAdapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onEventClick(event: Event) {
        val eventDetailFragment = EventDetailFragment().apply {
            arguments = Bundle().apply {
                event.id?.let { putInt("EVENT_ID", it) }
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, eventDetailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


