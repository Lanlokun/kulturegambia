package com.example.kulturgambia.data.repo

import android.content.Context
import com.example.kulturgambia.data.model.CultureItem
import com.example.kulturgambia.data.model.CultureItemsAsset
import com.example.kulturgambia.data.util.drawableIdByName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class AssetsCultureLoader(
    private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun load(): List<CultureItem> {
        val text = context.assets.open("culture_items.json")
            .bufferedReader()
            .use { it.readText() }

        val parsed: CultureItemsAsset = json.decodeFromString(text)

        return parsed.cultureItems.map { a ->
            CultureItem(
                id = a.id,
                title = a.title,
                category = a.category,
                imageRes = drawableIdByName(context, a.coverImageDrawable),
                description = "",
                summary = a.summary,
                content = a.content,
                coverImageUrl = "",
                galleryImageUrls = emptyList(),
                tags = a.tags,
                placeId = a.placeId,
                createdAt = a.createdAt
            )
        }
    }
}
