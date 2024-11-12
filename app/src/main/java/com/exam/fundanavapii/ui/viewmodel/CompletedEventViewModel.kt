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

class CompletedEventViewModel : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchCompletedEvents() {
        if (_events.value != null) {
            return
        }

        _isLoading.value = true
        val client = ApiClient.getInstance().getCompletedEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error!!) {
                        _events.value = responseBody.listEvents
                    } else {
                        _error.value = responseBody?.message
                    }
                } else {
                    _error.value = response.message()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message
            }
        })
    }
}
