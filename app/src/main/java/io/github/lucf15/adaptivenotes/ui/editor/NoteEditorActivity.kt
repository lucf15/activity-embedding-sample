package io.github.lucf15.adaptivenotes.ui.editor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.lucf15.adaptivenotes.platform.activity.NotesBaseActivity
import io.github.lucf15.adaptivenotes.ui.theme.FieldnotesTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteEditorActivity : NotesBaseActivity() {
    internal val model: NoteEditorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.open(intent.getStringExtra(EXTRA_NOTE_ID))
        lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.STARTED) { model.observe() } }
        setContent {
            FieldnotesTheme {
                NoteEditorScreen(model.state, model::onEvent, ::finish,
                    focusTitle = intent.getBooleanExtra(EXTRA_NEW_NOTE, false) && savedInstanceState == null)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        model.open(intent.getStringExtra(EXTRA_NOTE_ID))
    }

    companion object {
        const val EXTRA_NOTE_ID = "note_id"
        private const val EXTRA_NEW_NOTE = "new_note"
        fun intent(context: Context, id: String, isNew: Boolean = false): Intent =
            Intent(context, NoteEditorActivity::class.java).putExtra(EXTRA_NOTE_ID, id).putExtra(EXTRA_NEW_NOTE, isNew)
    }
}
