package io.github.lucf15.adaptivenotes.ui.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lucf15.adaptivenotes.domain.model.Note
import io.github.lucf15.adaptivenotes.domain.model.NoteCategory
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus
import io.github.lucf15.adaptivenotes.domain.repository.NotesRepository
import io.github.lucf15.adaptivenotes.domain.usecase.CreateNote
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NotesRepository,
    private val createNote: CreateNote,
    private val savedState: SavedStateHandle,
) : ViewModel() {
    private class MutableState : NotesContract.State {
        override var query by mutableStateOf("")
        override var filter by mutableStateOf(NotesContract.Filter.All)
        override var notes by mutableStateOf<List<Note>>(emptyList())
        override var selectedId by mutableStateOf<String?>(null)
        override var storage by mutableStateOf(StorageStatus.Loading)
        override var creating by mutableStateOf(false)
        override var actionFailed by mutableStateOf(false)
    }

    private val mutableState = MutableState().apply {
        query = savedState["query"] ?: ""
        filter = NotesContract.Filter.entries.firstOrNull { it.name == savedState.get<String>("filter") }
            ?: NotesContract.Filter.All
        selectedId = savedState["selected"]
    }
    val state: NotesContract.State = mutableState
    private val navigation = Channel<NotesContract.OpenEditor>(Channel.BUFFERED)
    val effects = navigation.receiveAsFlow()

    suspend fun observe(): Nothing = coroutineScope {
        launch { repository.notes.collect { updateNotes() } }
        launch { repository.status.collect { mutableState.storage = it } }
        kotlinx.coroutines.awaitCancellation()
    }

    fun onEvent(event: NotesContract.Event) {
        when (event) {
            is NotesContract.Event.QueryChanged -> {
                Snapshot.withMutableSnapshot {
                    mutableState.query = event.query
                    updateNotes()
                }
                savedState["query"] = event.query
            }
            is NotesContract.Event.FilterChanged -> {
                Snapshot.withMutableSnapshot {
                    mutableState.filter = event.filter
                    updateNotes()
                }
                savedState["filter"] = event.filter.name
            }
            is NotesContract.Event.OpenNote -> onOpenNote(event.id)
            NotesContract.Event.CreateNote -> onCreateNote()
            NotesContract.Event.Retry -> {
                mutableState.actionFailed = false
                repository.retry()
            }
        }
    }

    private fun onOpenNote(id: String, isNew: Boolean = false) {
        mutableState.selectedId = id
        savedState["selected"] = id
        navigation.trySend(NotesContract.OpenEditor(id, isNew))
    }

    private fun onCreateNote() {
        if (state.creating) return
        mutableState.creating = true
        viewModelScope.launch {
            try {
                onOpenNote(createNote().id, isNew = true)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                mutableState.actionFailed = true
            } finally {
                mutableState.creating = false
            }
        }
    }

    private fun updateNotes() {
        val query = state.query.trim()
        mutableState.notes = repository.notes.value.filter { note ->
            val matchesFilter = when (state.filter) {
                NotesContract.Filter.All -> true
                NotesContract.Filter.Pinned -> note.pinned
                NotesContract.Filter.Ideas -> note.category == NoteCategory.Ideas
            }
            matchesFilter && (query.isBlank() || note.title.contains(query, true) || note.body.contains(query, true))
        }.sortedWith(compareByDescending<Note> { it.pinned }.thenByDescending { it.updatedAt })
    }
}
