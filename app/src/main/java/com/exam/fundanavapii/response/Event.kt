package com.exam.fundanavapii.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class EventResponse (
    @field:SerializedName("listEvents")
    val listEvents: List<Event> = listOf(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("event")
    val event: Event? = null
)

@Parcelize
data class Events(
    var id: Int? = null,
    val name: String? = null,
    val mediaCover: String? = null,
    val registrants: Int? = null,
    val summary: String? = null,
    val imageLogo: String? = null,
    val link: String? = null,
    val description: String? = null,
    val ownerName: String? = null,
    val cityName: String? = null,
    val quota: Int? = null,
    val beginTime: String? = null,
    val endTime: String? = null,
    val category: String? = null,
    var isExpired: Boolean,
    var isFavorite: Boolean
) : Parcelable

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false) var id: Int,
    var name: String = "",
    var mediaCover: String? = null,
    var ownerName: String? = null,
    var imageLogo: String? = null
)

@Parcelize
data class Event (
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("imageLogo")
    val imageLogo: String? = null,

    @field:SerializedName("mediaCover")
    val mediaCover: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("ownerName")
    val ownerName: String? = null,

    @field:SerializedName("cityName")
    val cityName: String? = null,

    @field:SerializedName("quota")
    val quota: Int? = null,

    @field:SerializedName("registrants")
    val registrants: Int? = null,

    @field:SerializedName("beginTime")
    val beginTime: String? = null,

    @field:SerializedName("endTime")
    val endTime: String? = null,

    @field:SerializedName("link")
    val link: String? = null
): Parcelable