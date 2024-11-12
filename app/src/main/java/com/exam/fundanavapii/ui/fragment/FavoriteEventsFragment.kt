package com.exam.fundanavapii.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.exam.fundanavapii.R
import com.exam.fundanavapii.databinding.ActivityFavoriteEventsBinding
import com.exam.fundanavapii.response.FavoriteEvent
import com.exam.fundanavapii.ui.adapter.FavoriteEventsAdapter
import com.exam.fundanavapii.ui.viewmodel.FavoriteViewModel
import kotlinx.coroutines.launch

class FavoriteEventsFragment : Fragment() {
    private var _binding: ActivityFavoriteEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityFavoriteEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        adapter = FavoriteEventsAdapter { event ->
            navigateToDetail(event)
        }

        binding.rvFavoriteEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FavoriteEventsFragment.adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allFavorites.collect { favorites ->
                    if (favorites.isEmpty()) {
                        binding.tvEmptyFavorites.visibility = View.VISIBLE
                        binding.rvFavoriteEvents.visibility = View.GONE
                    } else {
                        binding.tvEmptyFavorites.visibility = View.GONE
                        binding.rvFavoriteEvents.visibility = View.VISIBLE
                        adapter.submitList(favorites)
                    }
                }
            }
        }
    }

    private fun navigateToDetail(event: FavoriteEvent) {
        val fragment = EventDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("EVENT_ID", event.id)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


