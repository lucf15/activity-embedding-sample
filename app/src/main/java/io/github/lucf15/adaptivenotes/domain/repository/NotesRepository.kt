package io.github.lucf15.adaptivenotes.domain.repository

import io.github.lucf15.adaptivenotes.domain.model.Note
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus
import kotlinx.coroutines.flow.StateFlow

interface NotesRepository {
    val notes: StateFlow<List<Note>>
    val status: StateFlow<StorageStatus>
    fun save(note: Note)
    fun retry()
    suspend fun awaitReady()
    suspend fun awaitSaved()
}
