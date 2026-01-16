package com.example.kulturgambia.data.repository

import com.example.kulturgambia.data.local.favorites.FavoriteEntity
import com.example.kulturgambia.data.local.favorites.FavoritesDao

class FavoritesRepository(private val dao: FavoritesDao) {

    suspend fun getAllIds(): Set<String> = dao.getAllIds().toSet()

    suspend fun isFavorite(id: String): Boolean = dao.isFavorite(id)

    suspend fun add(id: String) = dao.add(FavoriteEntity(id))

    suspend fun remove(id: String) = dao.remove(id)
}
