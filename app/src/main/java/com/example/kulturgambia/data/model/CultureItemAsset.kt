package com.example.kulturgambia.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CultureItemsAsset(
    val cultureItems: List<CultureItemAsset>
)

@Serializable
data class CultureItemAsset(
    val id: String,
    val title: String,
    val category: String,
    val summary: String,
    val content: String,
    val coverImageDrawable: String = "",
    val galleryImageDrawables: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val placeId: String? = null,
    val createdAt: Long = 0L
)
