package io.github.lucf15.adaptivenotes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus
import io.github.lucf15.adaptivenotes.ui.theme.NoteColors

@Composable
fun StorageMessage(status: StorageStatus, retry: () -> Unit) {
    if (status == StorageStatus.ReadError || status == StorageStatus.WriteError) {
        Text(
            if (status == StorageStatus.ReadError) "Couldn't open your notes. Tap to retry."
            else "Changes haven't been saved. Tap to retry.",
            Modifier.clickable(onClick = retry).padding(vertical = 12.dp),
            color = NoteColors.Peach, fontSize = 13.sp,
        )
    }
}
