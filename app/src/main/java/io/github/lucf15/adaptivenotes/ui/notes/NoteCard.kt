package io.github.lucf15.adaptivenotes.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import io.github.lucf15.adaptivenotes.ui.components.NoteIcons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lucf15.adaptivenotes.domain.model.Note
import io.github.lucf15.adaptivenotes.domain.model.NoteCategory
import io.github.lucf15.adaptivenotes.ui.components.NoteArtwork
import io.github.lucf15.adaptivenotes.ui.components.SoftCard
import io.github.lucf15.adaptivenotes.ui.theme.NoteColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteCard(note: Note, selected: Boolean, onClick: () -> Unit) {
    val locale = LocalLocale.current.platformLocale
    val accent = when (note.category) {
        NoteCategory.Ideas -> NoteColors.Accent
        NoteCategory.Work -> NoteColors.Peach
        NoteCategory.Personal -> NoteColors.Green
    }
    SoftCard(Modifier.fillMaxWidth().semantics { this.selected = selected }, selected, onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(note.category.name.uppercase(Locale.ROOT), color = accent, fontSize = 10.sp,
                letterSpacing = 1.5.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            if (note.pinned) Icon(NoteIcons.PushPin, "Pinned", Modifier.size(15.dp), tint = NoteColors.Secondary)
        }
        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(note.title.ifBlank { "Untitled note" }, color = NoteColors.Text, fontSize = 21.sp,
                    lineHeight = 27.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                Text(note.body.ifBlank { "A fresh page. Make it yours." }.replace('\n', ' '),
                    color = NoteColors.Secondary, fontSize = 13.sp, lineHeight = 20.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            NoteArtwork(Modifier.padding(start = 8.dp).size(58.dp), accent)
        }
        Spacer(Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(SimpleDateFormat("MMM d", locale).format(Date(note.updatedAt)),
                color = NoteColors.Secondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
            Icon(NoteIcons.ArrowOutward, null, Modifier.size(16.dp), tint = NoteColors.Secondary)
        }
    }
}
