package io.github.lucf15.adaptivenotes.ui.editor

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.TextFieldValue
import io.github.lucf15.adaptivenotes.domain.model.NoteCategory
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus

object NoteEditorContract {
    @Stable
    interface State {
        val noteId: String?
        val title: TextFieldValue
        val body: TextFieldValue
        val category: NoteCategory
        val pinned: Boolean
        val loading: Boolean
        val missing: Boolean
        val storage: StorageStatus
    }

    sealed interface Event {
        data class TitleChanged(val value: TextFieldValue) : Event
        data class BodyChanged(val value: TextFieldValue) : Event
        data object TogglePinned : Event
        data object Retry : Event
    }
}
