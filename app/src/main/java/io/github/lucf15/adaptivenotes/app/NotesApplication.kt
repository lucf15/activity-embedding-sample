package io.github.lucf15.adaptivenotes.app

import android.app.Application
import io.github.lucf15.adaptivenotes.app.di.notesDataModule
import io.github.lucf15.adaptivenotes.app.di.notesPresentationModule
import io.github.lucf15.adaptivenotes.platform.embedding.EmbeddingSetup
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EmbeddingSetup.register(this)
        startKoin {
            androidContext(this@NotesApplication)
            modules(notesDataModule, notesPresentationModule)
        }
    }
}
