package io.github.lucf15.adaptivenotes.ui.placeholder

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

class NotePlaceholderActivity : NotesBaseActivity() {
    private val model: NotePlaceholderViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.effects.collect { startActivity(NoteEditorActivity.intent(this@NotePlaceholderActivity, it, true)) }
            }
        }
        setContent { FieldnotesTheme { NotePlaceholderScreen(model.state, model::onEvent) } }
    }
}
