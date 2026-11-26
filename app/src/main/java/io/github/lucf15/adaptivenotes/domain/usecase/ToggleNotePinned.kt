package io.github.lucf15.adaptivenotes.domain.usecase

import io.github.lucf15.adaptivenotes.domain.repository.NotesRepository

class ToggleNotePinned(private val repository: NotesRepository) {
    operator fun invoke(id: String) {
        val note = repository.notes.value.firstOrNull { it.id == id } ?: return
        repository.save(note.copy(pinned = !note.pinned))
    }
}
