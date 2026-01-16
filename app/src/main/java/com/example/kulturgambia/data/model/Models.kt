package com.example.kulturgambia.data.model

import kotlinx.serialization.Serializable
import androidx.annotation.DrawableRes


@Serializable
data class CultureItem(
    val id: String,
    val title: String,
    val category: String,
    @DrawableRes val imageRes: Int? = null,
    val description: String = "",
    val summary: String,
    val content: String,
    val coverImageUrl: String = "",
    val galleryImageUrls: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val placeId: String? = null,
    val createdAt: Long = 0L
)

@Serializable
data class Place(
    val id: String,
    val name: String,
    val type: String,
    val lat: Double,
    val lng: Double,
    val address: String,
    val description: String,
    val imageUrl: String = ""
)

@Serializable
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val placeId: String,
    val imageUrl: String = ""
)
