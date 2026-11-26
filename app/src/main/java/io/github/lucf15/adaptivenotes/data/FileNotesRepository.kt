package io.github.lucf15.adaptivenotes.data

import android.util.AtomicFile
import android.util.Log
import io.github.lucf15.adaptivenotes.domain.model.Note
import io.github.lucf15.adaptivenotes.domain.model.NoteCategory
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus
import io.github.lucf15.adaptivenotes.domain.repository.NotesRepository
import java.io.File
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class FileNotesRepository(directory: File, scope: CoroutineScope) : NotesRepository {
    private val file = AtomicFile(File(directory, "notes.json"))
    private val mutableNotes = MutableStateFlow<List<Note>>(emptyList())
    private val mutableStatus = MutableStateFlow(StorageStatus.Loading)
    private val ready = CompletableDeferred<Unit>()
    private val revision = AtomicLong()
    // A queued snapshot includes every note, so conflation retains edits across activities.
    private val commands = Channel<Command>(Channel.CONFLATED)
    private val publicationLock = Any()
    override val notes = mutableNotes.asStateFlow()
    override val status = mutableStatus.asStateFlow()

    private sealed interface Command {
        data class Write(val revision: Long, val notes: List<Note>) : Command
        data object Reload : Command
    }

    init {
        scope.launch {
            load()
            for (command in commands) {
                when (command) {
                    Command.Reload -> load()
                    is Command.Write -> {
                        try {
                            persist(command.notes)
                            synchronized(publicationLock) {
                                if (revision.get() == command.revision) mutableStatus.value = StorageStatus.Saved
                            }
                        } catch (error: Exception) {
                            Log.e("FieldnotesStorage", "Could not persist notes", error)
                            synchronized(publicationLock) {
                                if (revision.get() == command.revision) mutableStatus.value = StorageStatus.WriteError
                            }
                        }
                    }
                }
            }
        }
    }

    override fun save(note: Note) = synchronized(publicationLock) {
        check(status.value !in setOf(StorageStatus.Loading, StorageStatus.ReadError))
        mutableNotes.update { notes ->
            if (notes.any { it.id == note.id }) notes.map { if (it.id == note.id) note else it }
            else notes + note
        }
        enqueueWrite()
    }

    override fun retry() {
        if (status.value == StorageStatus.ReadError) {
            mutableStatus.value = StorageStatus.Loading
            commands.trySend(Command.Reload)
        } else if (status.value == StorageStatus.WriteError) enqueueWrite()
    }

    override suspend fun awaitReady() {
        ready.await()
        val result = status.first { it != StorageStatus.Loading }
        check(result != StorageStatus.ReadError) { "Notes could not be read" }
    }

    override suspend fun awaitSaved() {
        awaitReady()
        val result = status.first { it == StorageStatus.Saved || it == StorageStatus.WriteError }
        check(result == StorageStatus.Saved) { "Notes could not be saved" }
    }

    private fun enqueueWrite() = synchronized(publicationLock) {
        val current = revision.incrementAndGet()
        mutableStatus.value = StorageStatus.Saving
        check(commands.trySend(Command.Write(current, notes.value)).isSuccess)
    }

    private fun load() {
        try {
            val loaded = if (file.baseFile.exists() || File(file.baseFile.path + ".bak").exists()) {
                file.openRead().bufferedReader().use { decode(it.readText()) }
            } else sampleNotes().also(::persist)
            mutableNotes.value = loaded
            mutableStatus.value = StorageStatus.Saved
        } catch (error: Exception) {
            Log.e("FieldnotesStorage", "Could not read notes; original file retained", error)
            mutableStatus.value = StorageStatus.ReadError
        } finally {
            ready.complete(Unit)
        }
    }

    private fun persist(notes: List<Note>) {
        val output = file.startWrite()
        try {
            val array = JSONArray()
            notes.forEach { note ->
                array.put(JSONObject().put("id", note.id).put("title", note.title).put("body", note.body)
                    .put("category", note.category.name).put("pinned", note.pinned).put("updatedAt", note.updatedAt))
            }
            output.write(JSONObject().put("version", 1).put("notes", array).toString().toByteArray(Charsets.UTF_8))
            file.finishWrite(output)
        } catch (error: Exception) {
            file.failWrite(output)
            throw error
        }
    }

    private fun decode(text: String): List<Note> {
        val root = JSONObject(text)
        require(root.getInt("version") == 1)
        val array = root.getJSONArray("notes")
        return List(array.length()) { index ->
            val value = array.getJSONObject(index)
            Note(value.getString("id"), value.getString("title"), value.getString("body"),
                NoteCategory.valueOf(value.getString("category")), value.getBoolean("pinned"), value.getLong("updatedAt"))
        }.also { values -> require(values.map { it.id }.distinct().size == values.size) }
    }
}
