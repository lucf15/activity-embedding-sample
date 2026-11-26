package io.github.lucf15.adaptivenotes.domain.usecase

import io.github.lucf15.adaptivenotes.domain.model.Note
import io.github.lucf15.adaptivenotes.domain.model.NoteCategory
import io.github.lucf15.adaptivenotes.domain.repository.NotesRepository

class CreateNote(
    private val repository: NotesRepository,
    private val newId: () -> String,
    private val now: () -> Long,
) {
    suspend operator fun invoke(): Note {
        repository.awaitReady()
        return Note(newId(), "", "", NoteCategory.Ideas, false, now()).also(repository::save)
    }
}
