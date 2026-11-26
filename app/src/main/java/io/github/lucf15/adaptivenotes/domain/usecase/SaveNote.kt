package io.github.lucf15.adaptivenotes.domain.usecase

import io.github.lucf15.adaptivenotes.domain.repository.NotesRepository

class SaveNote(private val repository: NotesRepository, private val now: () -> Long) {
    operator fun invoke(id: String, title: String, body: String) {
        val note = repository.notes.value.firstOrNull { it.id == id } ?: return
        if (title == note.title && body == note.body) return
        repository.save(note.copy(title = title, body = body, updatedAt = now()))
    }
}
