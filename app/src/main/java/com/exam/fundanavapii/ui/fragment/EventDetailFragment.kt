package com.exam.fundanavapii.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exam.fundanavapii.R
import com.exam.fundanavapii.databinding.ActivityDetailsEventsBinding
import com.exam.fundanavapii.response.FavoriteEvent
import com.exam.fundanavapii.ui.adapter.EventDetailAdapter
import com.exam.fundanavapii.ui.viewmodel.EventDetailViewModel
import com.exam.fundanavapii.ui.viewmodel.FavoriteViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EventDetailFragment : Fragment() {

    private lateinit var viewModel: EventDetailViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: EventDetailAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var _binding: ActivityDetailsEventsBinding? = null
    private val binding get() = _binding!!
    //private var currentEvent: FavoriteEvent? = null

    private var eventId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityDetailsEventsBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = binding.recyclerViewEventDetails
        progressBar = binding.progressBar

        eventId = arguments?.getInt("EVENT_ID") ?: return view

        viewModel = ViewModelProvider(this)[EventDetailViewModel::class.java]
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        adapter = EventDetailAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        observeViewModel()

        eventId?.let { viewModel.fetchEventDetails(it) }

        binding.btnRegister.setOnClickListener {
            eventId?.let { openRegistrationLink() }
        }

        eventId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    favoriteViewModel.isFavorite(id).collect { isFavorite ->
                        binding.fab.setImageResource(
                            if (isFavorite) R.drawable.ic_favorite_filled
                            else R.drawable.ic_favorite_border
                        )
                    }
                }
            }
        }

        binding.fab.setOnClickListener {
            toggleFavorite()
        }

        return view
    }

    private fun observeViewModel() {
        viewModel.eventDetails.observe(viewLifecycleOwner) { event ->
            Log.d("EventDetailFragment", "Event Details: $event")
            event?.let {
                Glide.with(this)
                    .load(it.mediaCover)
                    .into(binding.ivEventBanner)

                binding.tvEventTitle.text = it.name
                binding.tvEventOwner.text = it.ownerName
                binding.tvEventCity.text = it.cityName
                binding.tvEventTime.text = it.beginTime
                val remainingQuota = it.quota?.minus(it.registrants ?: 0) ?: 0
                val totalQuota = it.quota ?: 0
                binding.tvEventQuota.text = getString(R.string.remaining_quota, remainingQuota, totalQuota)
                binding.tvEventRegistrants.text = getString(R.string.registrants_count, it.registrants ?: 0)
                binding.tvEventDescription.text = Html.fromHtml(it.description, Html.FROM_HTML_MODE_LEGACY)
                binding.tvEventCategory.text = Html.fromHtml("<b>Category :</b> ${it.category}", Html.FROM_HTML_MODE_LEGACY)

                binding.btnRegister.tag = it.link ?: ""

                val details = listOf(
                    Pair("Event Name", it.name),
                    Pair("Owner", it.ownerName),
                    Pair("City", it.cityName),
                    Pair("Time", it.beginTime),
                    Pair("Quota", getString(R.string.remaining_quota, remainingQuota, totalQuota)),
                    Pair("Registrants", getString(R.string.registrants_count, it.registrants ?: 0)),
                    Pair("Description", Html.fromHtml(it.description, Html.FROM_HTML_MODE_LEGACY)),
                    Pair("Category", Html.fromHtml("<b>Category :</b> ${it.category}", Html.FROM_HTML_MODE_LEGACY))
                )
                adapter = EventDetailAdapter(details)
                recyclerView.adapter = adapter
            }
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

    private fun toggleFavorite() {
        viewModel.eventDetails.value?.let { event ->
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val isFavorite = favoriteViewModel.isFavorite(event.id ?: return@launch).first()
                    if (isFavorite) {
                        favoriteViewModel.removeFromFavorite(
                            FavoriteEvent(
                                id = event.id,
                                name = event.name ?: "",
                                ownerName = event.ownerName ?: "",
                                mediaCover = event.mediaCover,
                                imageLogo = event.imageLogo
                            )
                        )
                    } else {
                        favoriteViewModel.addToFavorite(event)
                    }
                } catch (e: Exception) {
                    Log.e("EventDetailFragment", "Error handling favorite: ${e.message}")
                    Toast.makeText(requireContext(), "Error updating favorite status", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun openRegistrationLink() {
        val registrationLink = binding.btnRegister.tag.toString()
        if (registrationLink.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(registrationLink))
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "No registration link available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}