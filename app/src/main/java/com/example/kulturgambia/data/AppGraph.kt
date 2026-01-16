package com.example.kulturgambia.data


import android.app.Application
import com.example.kulturgambia.data.local.favorites.FavoritesDatabase
import com.example.kulturgambia.data.repository.*

class AppGraph private constructor(app: Application) {

    val cultureRepository = CultureRepository(app)
    val placesRepository = PlacesRepository(app)
    val eventsRepository = EventsRepository(app)

    private val favoritesDb = FavoritesDatabase.get(app)
    val favoritesRepository = FavoritesRepository(favoritesDb.dao())

    companion object {
        // call from Compose via AppGraph.current()
        fun current(): AppGraph {
            val app = AppInstance.app
            return AppGraph(app)
        }
    }
}

/**
 * Minimal app instance holder.
 * Add this Application class to manifest if you want, but we keep it super simple:
 * Android will instantiate it automatically if registered in manifest.
 */
object AppInstance {
    lateinit var app: Application
        private set

    fun init(application: Application) {
        app = application
    }
}
