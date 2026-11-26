package io.github.lucf15.adaptivenotes.app.di

import io.github.lucf15.adaptivenotes.data.FileNotesRepository
import io.github.lucf15.adaptivenotes.domain.repository.NotesRepository
import io.github.lucf15.adaptivenotes.domain.usecase.CreateNote
import io.github.lucf15.adaptivenotes.domain.usecase.SaveNote
import io.github.lucf15.adaptivenotes.domain.usecase.ToggleNotePinned
import io.github.lucf15.adaptivenotes.ui.editor.NoteEditorViewModel
import io.github.lucf15.adaptivenotes.ui.notes.NotesViewModel
import io.github.lucf15.adaptivenotes.ui.placeholder.NotePlaceholderViewModel
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val notesDataModule = module {
    single(named("persistence")) { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    single<NotesRepository> { FileNotesRepository(androidContext().filesDir, get(named("persistence"))) }
    factory { CreateNote(get(), { UUID.randomUUID().toString() }, System::currentTimeMillis) }
    factory { SaveNote(get(), System::currentTimeMillis) }
    factory { ToggleNotePinned(get()) }
}

val notesPresentationModule = module {
    viewModel { NotesViewModel(get(), get(), get()) }
    viewModel { NoteEditorViewModel(get(), get(), get(), get()) }
    viewModel { NotePlaceholderViewModel(get()) }
}
