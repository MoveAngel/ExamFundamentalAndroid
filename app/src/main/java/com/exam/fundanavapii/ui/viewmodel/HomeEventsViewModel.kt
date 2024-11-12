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

class HomeEventsViewModel : ViewModel() {
    private val _activeEvents = MutableLiveData<List<Event>>()
    val activeEvents: LiveData<List<Event>> get() = _activeEvents

    private val _completedEvents = MutableLiveData<List<Event>>()
    val completedEvents: LiveData<List<Event>> get() = _completedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchEvents() {
        fetchActiveEvents()
        fetchCompletedEvents()
    }

    private fun fetchActiveEvents() {
        _isLoading.value = true
        val client = ApiClient.getInstance().getActiveEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        if (!responseBody.error!!) {
                            _activeEvents.value = responseBody.listEvents.take(5)
                        } else {
                            _error.value = responseBody.message
                        }
                    } ?: run {
                        _error.value = "Error fetching data"
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

    private fun fetchCompletedEvents() {
        _isLoading.value = true
        val client = ApiClient.getInstance().getCompletedEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        if (!responseBody.error!!) {
                            _completedEvents.value = responseBody.listEvents.take(5)
                        } else {
                            _error.value = responseBody.message
                        }
                    } ?: run {
                        _error.value = "Error fetching data"
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