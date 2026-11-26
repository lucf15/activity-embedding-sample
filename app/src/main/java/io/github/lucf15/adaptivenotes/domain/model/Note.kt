package io.github.lucf15.adaptivenotes.domain.model

data class Note(
    val id: String,
    val title: String,
    val body: String,
    val category: NoteCategory,
    val pinned: Boolean,
    val updatedAt: Long,
)

enum class NoteCategory { Ideas, Work, Personal }

enum class StorageStatus { Loading, Saved, Saving, ReadError, WriteError }
