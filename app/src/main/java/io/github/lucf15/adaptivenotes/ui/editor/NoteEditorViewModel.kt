package io.github.lucf15.adaptivenotes.ui.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lucf15.adaptivenotes.domain.model.NoteCategory
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus
import io.github.lucf15.adaptivenotes.domain.repository.NotesRepository
import io.github.lucf15.adaptivenotes.domain.usecase.SaveNote
import io.github.lucf15.adaptivenotes.domain.usecase.ToggleNotePinned
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class NoteEditorViewModel(
    private val repository: NotesRepository,
    private val saveNote: SaveNote,
    private val toggleNotePinned: ToggleNotePinned,
    private val savedState: SavedStateHandle,
) : ViewModel() {
    private class MutableState : NoteEditorContract.State {
        override var noteId by mutableStateOf<String?>(null)
        override var title by mutableStateOf(TextFieldValue())
        override var body by mutableStateOf(TextFieldValue())
        override var category by mutableStateOf(NoteCategory.Ideas)
        override var pinned by mutableStateOf(false)
        override var loading by mutableStateOf(true)
        override var missing by mutableStateOf(false)
        override var storage by mutableStateOf(StorageStatus.Loading)
    }
    private val mutableState = MutableState()
    val state: NoteEditorContract.State = mutableState
    private var opening: Job? = null

    fun open(id: String?) {
        if (state.noteId == id && !state.loading && !state.missing) return
        opening?.cancel()
        Snapshot.withMutableSnapshot {
            mutableState.noteId = id
            mutableState.loading = true
            mutableState.missing = false
        }
        opening = viewModelScope.launch {
            try {
                repository.awaitReady()
                val note = repository.notes.value.firstOrNull { it.id == id }
                Snapshot.withMutableSnapshot {
                    if (note == null) {
                        mutableState.missing = true
                    } else {
                        val restoring = savedState.get<String>("draft_note") == id
                        mutableState.title = restoreField("title", note.title, restoring)
                        mutableState.body = restoreField("body", note.body, restoring)
                        mutableState.category = note.category
                        mutableState.pinned = note.pinned
                    }
                    mutableState.loading = false
                }
                if (note != null) saveDraft()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                mutableState.loading = false
                mutableState.missing = true
            }
        }
    }

    suspend fun observe(): Nothing = coroutineScope {
        launch { repository.status.collect { mutableState.storage = it } }
        launch {
            repository.notes.collect { notes ->
                notes.firstOrNull { it.id == state.noteId }?.let { mutableState.pinned = it.pinned }
            }
        }
        kotlinx.coroutines.awaitCancellation()
    }

    fun onEvent(event: NoteEditorContract.Event) {
        if (state.loading || state.missing) {
            if (event == NoteEditorContract.Event.Retry) {
                repository.retry()
                open(state.noteId)
            }
            return
        }
        when (event) {
            is NoteEditorContract.Event.TitleChanged -> { mutableState.title = event.value; saveDraft() }
            is NoteEditorContract.Event.BodyChanged -> { mutableState.body = event.value; saveDraft() }
            NoteEditorContract.Event.TogglePinned -> state.noteId?.let(toggleNotePinned::invoke)
            NoteEditorContract.Event.Retry -> repository.retry()
        }
    }

    private fun saveDraft() {
        val id = state.noteId ?: return
        savedState["draft_note"] = id
        saveField("title", state.title)
        saveField("body", state.body)
        saveNote(id, state.title.text, state.body.text)
    }

    private fun saveField(key: String, value: TextFieldValue) {
        savedState[key] = value.text
        savedState["${key}_start"] = value.selection.start
        savedState["${key}_end"] = value.selection.end
    }

    private fun restoreField(key: String, fallback: String, restoring: Boolean): TextFieldValue {
        if (!restoring) return TextFieldValue(fallback)
        val text = savedState.get<String>(key) ?: fallback
        return TextFieldValue(text, TextRange(
            (savedState.get<Int>("${key}_start") ?: 0).coerceIn(0, text.length),
            (savedState.get<Int>("${key}_end") ?: 0).coerceIn(0, text.length),
        ))
    }
}
