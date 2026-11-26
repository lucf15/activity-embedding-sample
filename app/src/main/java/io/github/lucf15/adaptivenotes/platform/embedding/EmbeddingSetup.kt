package io.github.lucf15.adaptivenotes.platform.embedding

import android.content.Context
import android.annotation.SuppressLint
import android.util.Log
import androidx.window.WindowSdkExtensions
import androidx.window.RequiresWindowSdkExtension
import androidx.window.embedding.ActivityEmbeddingController
import androidx.window.embedding.EmbeddingConfiguration
import androidx.window.embedding.RuleController
import androidx.window.embedding.SplitController
import io.github.lucf15.adaptivenotes.BuildConfig
import io.github.lucf15.adaptivenotes.R

object EmbeddingSetup {
    fun register(context: Context) {
        if (!BuildConfig.EMBEDDING_ENABLED) return
        RuleController.getInstance(context).setRules(RuleController.parseRules(context, R.xml.activity_embedding_rules))
        val extension = WindowSdkExtensions.getInstance().extensionVersion
        enableStateRestorationIfSupported(context)
        Log.d("FieldnotesEmbedding", "support=${SplitController.getInstance(context).splitSupportStatus} extension=$extension")
    }

    // Lint 32.4 flags this Kotlin property guard; keep the runtime check at this boundary.
    @SuppressLint("RequiresWindowSdk")
    private fun enableStateRestorationIfSupported(context: Context) {
        if (WindowSdkExtensions.getInstance().extensionVersion >= 8) {
            enableStateRestoration(context)
        }
    }

    @RequiresWindowSdkExtension(8)
    private fun enableStateRestoration(context: Context) {
        ActivityEmbeddingController.getInstance(context).setEmbeddingConfiguration(
            EmbeddingConfiguration.Builder().setAutoSaveEmbeddingState(true).build()
        )
    }
}
