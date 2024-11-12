package com.exam.fundanavapii.data

import com.exam.fundanavapii.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getActiveEvents(@Query("active") active: Int = 1): Call<EventResponse>

    @GET("events")
    fun getCompletedEvents(@Query("active") active: Int = 0): Call<EventResponse>

    @GET("events/{id}")
    fun getEventsDetails(@Path("id") id: Int) : Call<EventResponse>
}