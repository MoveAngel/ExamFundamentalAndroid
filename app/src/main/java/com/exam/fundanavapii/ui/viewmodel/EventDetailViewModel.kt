package com.exam.fundanavapii.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exam.fundanavapii.data.ApiClient
import com.exam.fundanavapii.response.Event
import com.exam.fundanavapii.response.EventResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailViewModel : ViewModel() {
    private val _eventDetails = MutableLiveData<Event?>()
    val eventDetails: MutableLiveData<Event?> get() = _eventDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchEventDetails(eventId: Int) {
        _isLoading.value = true
        val client = ApiClient.getInstance().getEventsDetails(eventId)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { eventResponse ->
                        if (!eventResponse.error!!) {
                            _eventDetails.value = eventResponse.event
                        } else {
                            _error.value = eventResponse.message
                        }
                    } ?: run {
                        _error.value = "Error fetching event details"
                    }
                } else {
                    _error.value = response.message()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message ?: "Unknown error"
            }
        })
    }
}

