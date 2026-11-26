package io.github.lucf15.adaptivenotes.ui.placeholder

import androidx.compose.runtime.Stable

object NotePlaceholderContract {
    @Stable
    interface State {
        val creating: Boolean
        val failed: Boolean
    }
    sealed interface Event { data object CreateNote : Event }
}
