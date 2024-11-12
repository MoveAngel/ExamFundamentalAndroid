package com.exam.fundanavapii.ui.repository

import com.exam.fundanavapii.data.FavoriteEventDao
import com.exam.fundanavapii.response.FavoriteEvent
import kotlinx.coroutines.flow.Flow

class FavoriteEventRepository(private val favoriteEventDao: FavoriteEventDao) {
    val allFavorites: Flow<List<FavoriteEvent>> = favoriteEventDao.getAllFavorites()

    fun getFavoriteById(id: Int): Flow<FavoriteEvent?> = favoriteEventDao.getFavoriteById(id)

    fun isFavorite(id: Int): Flow<Boolean> = favoriteEventDao.isFavorite(id)

    suspend fun insert(favoriteEvent: FavoriteEvent) {
        favoriteEventDao.insert(favoriteEvent)
    }

    suspend fun delete(favoriteEvent: FavoriteEvent) {
        favoriteEventDao.delete(favoriteEvent)
    }
}