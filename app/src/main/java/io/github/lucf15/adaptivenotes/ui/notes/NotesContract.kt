package io.github.lucf15.adaptivenotes.ui.notes

import androidx.compose.runtime.Stable
import io.github.lucf15.adaptivenotes.domain.model.Note
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus

object NotesContract {
    enum class Filter { All, Pinned, Ideas }

    @Stable
    interface State {
        val query: String
        val filter: Filter
        val notes: List<Note>
        val selectedId: String?
        val storage: StorageStatus
        val creating: Boolean
        val actionFailed: Boolean
    }

    sealed interface Event {
        data class QueryChanged(val query: String) : Event
        data class FilterChanged(val filter: Filter) : Event
        data class OpenNote(val id: String) : Event
        data object CreateNote : Event
        data object Retry : Event
    }

    data class OpenEditor(val id: String, val isNew: Boolean = false)
}
