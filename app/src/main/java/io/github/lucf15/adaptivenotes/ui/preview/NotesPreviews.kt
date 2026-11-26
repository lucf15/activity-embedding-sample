package io.github.lucf15.adaptivenotes.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.lucf15.adaptivenotes.data.sampleNotes
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus
import io.github.lucf15.adaptivenotes.ui.editor.NoteEditorContract
import io.github.lucf15.adaptivenotes.ui.editor.NoteEditorScreen
import io.github.lucf15.adaptivenotes.ui.notes.NotesContract
import io.github.lucf15.adaptivenotes.ui.notes.NotesScreen
import io.github.lucf15.adaptivenotes.ui.placeholder.NotePlaceholderContract
import io.github.lucf15.adaptivenotes.ui.placeholder.NotePlaceholderScreen
import io.github.lucf15.adaptivenotes.ui.theme.FieldnotesTheme
import io.github.lucf15.adaptivenotes.ui.theme.NoteColors

private val notes = sampleNotes()
private val collection = object : NotesContract.State {
    override val query = ""
    override val filter = NotesContract.Filter.All
    override val notes = io.github.lucf15.adaptivenotes.ui.preview.notes
    override val selectedId = notes.first().id
    override val storage = StorageStatus.Saved
    override val creating = false
    override val actionFailed = false
}
private val editor = object : NoteEditorContract.State {
    private val note = notes.first()
    override val noteId = note.id
    override val title = TextFieldValue(note.title)
    override val body = TextFieldValue(note.body)
    override val category = note.category
    override val pinned = note.pinned
    override val loading = false
    override val missing = false
    override val storage = StorageStatus.Saved
}
private val placeholder = object : NotePlaceholderContract.State {
    override val creating = false
    override val failed = false
}

@Preview(name = "Collection · phone", widthDp = 412, heightDp = 892)
@Composable
private fun CollectionPreview() {
    FieldnotesTheme { NotesScreen(collection) {} }
}

@Preview(name = "Editor · phone", widthDp = 412, heightDp = 892)
@Composable
private fun EditorPreview() {
    FieldnotesTheme { NoteEditorScreen(editor, {}, {}, false) }
}

@Preview(name = "Open note · wide", widthDp = 1024, heightDp = 768)
@Composable
private fun OpenNotePreview() {
    FieldnotesTheme {
        Row(Modifier.fillMaxSize().background(NoteColors.Background)) {
            Box(Modifier.weight(.4f)) { NotesScreen(collection) {} }
            VerticalDivider(color = NoteColors.Border, modifier = Modifier.width(1.dp))
            Box(Modifier.weight(.6f)) { NoteEditorScreen(editor, {}, {}, false) }
        }
    }
}

@Preview(name = "Empty detail · wide", widthDp = 1024, heightDp = 768)
@Composable
private fun EmptyDetailPreview() {
    FieldnotesTheme {
        Row(Modifier.fillMaxSize()) {
            Box(Modifier.weight(.4f)) { NotesScreen(collection) {} }
            Box(Modifier.weight(.6f)) { NotePlaceholderScreen(placeholder) {} }
        }
    }
}
