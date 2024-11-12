package com.exam.fundanavapii.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exam.fundanavapii.response.FavoriteEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM favorite_events")
    fun getAllFavorites(): Flow<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_events WHERE id = :id")
    fun getFavoriteById(id: Int): Flow<FavoriteEvent?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEvent: FavoriteEvent)

    @Delete
    suspend fun delete(favoriteEvent: FavoriteEvent)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_events WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}


