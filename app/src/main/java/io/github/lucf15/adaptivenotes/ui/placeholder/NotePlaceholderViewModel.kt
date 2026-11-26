package io.github.lucf15.adaptivenotes.ui.placeholder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lucf15.adaptivenotes.domain.usecase.CreateNote
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotePlaceholderViewModel(private val createNote: CreateNote) : ViewModel() {
    private class MutableState : NotePlaceholderContract.State {
        override var creating by mutableStateOf(false)
        override var failed by mutableStateOf(false)
    }
    private val mutableState = MutableState()
    val state: NotePlaceholderContract.State = mutableState
    private val navigation = Channel<String>(Channel.BUFFERED)
    val effects = navigation.receiveAsFlow()

    fun onEvent(event: NotePlaceholderContract.Event) {
        if (event != NotePlaceholderContract.Event.CreateNote || state.creating) return
        mutableState.creating = true
        mutableState.failed = false
        viewModelScope.launch {
            try {
                navigation.send(createNote().id)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                mutableState.failed = true
            } finally {
                mutableState.creating = false
            }
        }
    }
}
