package io.github.lucf15.adaptivenotes.ui.notes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.lucf15.adaptivenotes.platform.activity.NotesBaseActivity
import io.github.lucf15.adaptivenotes.ui.editor.NoteEditorActivity
import io.github.lucf15.adaptivenotes.ui.theme.FieldnotesTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotesActivity : NotesBaseActivity() {
    internal val model: NotesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { model.observe() }
                launch {
                    model.effects.collect { editor ->
                        startActivity(NoteEditorActivity.intent(this@NotesActivity, editor.id, editor.isNew))
                    }
                }
            }
        }
        setContent { FieldnotesTheme { NotesScreen(model.state, model::onEvent) } }
    }
}
