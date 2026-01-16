// app/src/main/java/com/example/kulturgambia/data/repository/Repositories.kt
package com.example.kulturgambia.data.repository

import android.content.Context
import com.example.kulturgambia.data.local.JsonAssetLoader
import com.example.kulturgambia.data.model.CultureItem
import com.example.kulturgambia.data.model.CultureItemsAsset
import com.example.kulturgambia.data.model.Event
import com.example.kulturgambia.data.model.Place
import com.example.kulturgambia.data.util.drawableIdByName

class CultureRepository(private val context: Context) {

    private val loader = JsonAssetLoader(context)

    // ✅ decode wrapper object { "cultureItems": [...] }
    private val baseItems: List<CultureItem> by lazy {
        val parsed: CultureItemsAsset = loader.load("culture_items.json")
        parsed.cultureItems.map { a ->
            CultureItem(
                id = a.id,
                title = a.title,
                category = a.category,
                imageRes = drawableIdByName(context, a.coverImageDrawable),
                summary = a.summary,
                content = a.content,
                tags = a.tags,
                placeId = a.placeId,
                createdAt = a.createdAt
            )
        }
    }

    // user posts (in-memory for now)
    private val posts = mutableListOf<CultureItem>()

    fun getAll(): List<CultureItem> = (posts + baseItems).sortedByDescending { it.createdAt }

    fun getCategories(): List<String> = (posts + baseItems).map { it.category }.distinct().sorted()

    fun getByCategory(category: String): List<CultureItem> =
        (posts + baseItems).filter { it.category.equals(category, true) }
            .sortedByDescending { it.createdAt }

    fun getById(id: String): CultureItem? = (posts + baseItems).find { it.id == id }

    fun addPost(
        title: String,
        category: String,
        summary: String,
        content: String,
        coverImageDrawable: String? = null
    ): Boolean {
        return try {
            val id = "u_" + System.currentTimeMillis().toString()
            val resId = coverImageDrawable?.let { drawableIdByName(context, it) }

            posts.add(
                CultureItem(
                    id = id,
                    title = title,
                    category = category,
                    imageRes = resId,
                    summary = summary,
                    content = content,
                    tags = listOf("UserPost"),
                    createdAt = System.currentTimeMillis()
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }

}

class PlacesRepository(context: Context) {
    private val loader = JsonAssetLoader(context)

    // ✅ assumes places.json is a top-level array: [ ... ]
    private val places: List<Place> by lazy { loader.load("places.json") }

    fun getAll(): List<Place> = places
    fun getById(id: String): Place? = places.find { it.id == id }
}
class EventsRepository(context: Context) {
    private val loader = JsonAssetLoader(context)
    private val events: List<Event> by lazy { loader.load("events.json") }

    fun getUpcoming(): List<Event> = events.sortedBy { it.startDate }
    fun getById(id: String): Event? = events.find { it.id == id }
}

