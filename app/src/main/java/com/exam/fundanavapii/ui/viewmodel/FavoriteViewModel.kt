package com.exam.fundanavapii.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.exam.fundanavapii.response.Event
import com.exam.fundanavapii.response.FavoriteEvent
import com.exam.fundanavapii.ui.database.AppDatabase
import com.exam.fundanavapii.ui.repository.FavoriteEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteEventRepository
    val allFavorites: StateFlow<List<FavoriteEvent>>

    init {
        val dao = AppDatabase.getDatabase(application).favoriteEventDao()
        repository = FavoriteEventRepository(dao)
        allFavorites = repository.allFavorites.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )
    }

    //fun getFavoriteById(id: Int): Flow<FavoriteEvent?> = repository.getFavoriteById(id)

    fun isFavorite(id: Int): Flow<Boolean> = repository.isFavorite(id)

    fun addToFavorite(event: Event) {
        viewModelScope.launch {
            repository.insert(
                FavoriteEvent(
                    id = event.id ?: return@launch,
                    name = event.name ?: "",
                    ownerName = event.ownerName ?: "",
                    mediaCover = event.mediaCover,
                    imageLogo = event.imageLogo
                )
            )
        }
    }

    fun removeFromFavorite(event: FavoriteEvent) {
        viewModelScope.launch {
            repository.delete(event)
        }
    }
}
