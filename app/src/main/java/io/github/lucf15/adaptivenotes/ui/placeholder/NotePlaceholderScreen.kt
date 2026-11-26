package io.github.lucf15.adaptivenotes.ui.placeholder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import io.github.lucf15.adaptivenotes.ui.components.NoteIcons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lucf15.adaptivenotes.ui.components.NoteArtwork
import io.github.lucf15.adaptivenotes.ui.theme.NoteColors

@Composable
fun NotePlaceholderScreen(state: NotePlaceholderContract.State, onEvent: (NotePlaceholderContract.Event) -> Unit) {
    Box(Modifier.fillMaxSize().background(NoteColors.Background)
        .background(Brush.radialGradient(listOf(NoteColors.PlaceholderGlow, NoteColors.Background), radius = 1100f))
        .safeDrawingPadding(), Alignment.Center) {
        Column(Modifier.widthIn(max = 440.dp).verticalScroll(rememberScrollState()).padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            NoteArtwork(Modifier.size(168.dp))
            Spacer(Modifier.height(18.dp))
            Text("Room for your\nnext idea.", color = NoteColors.Text, fontSize = 38.sp, lineHeight = 45.sp,
                fontWeight = FontWeight.SemiBold, letterSpacing = (-1).sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(18.dp))
            Text("Pick a note or start something new.\nYour thoughts have a place here.",
                color = NoteColors.Secondary, fontSize = 15.sp, lineHeight = 25.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(30.dp))
            Row(Modifier.clip(CircleShape).background(NoteColors.Primary).clickable(
                enabled = !state.creating, role = Role.Button,
            ) { onEvent(NotePlaceholderContract.Event.CreateNote) }.padding(horizontal = 23.dp, vertical = 17.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                Icon(NoteIcons.Add, null, Modifier.size(19.dp), tint = Color.White)
                Text(if (state.creating) "Opening…" else "Write a note", color = Color.White,
                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            if (state.failed) Text("Couldn't create a note. Please try again.", Modifier.padding(top = 20.dp),
                color = NoteColors.Error, fontSize = 12.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(36.dp))
            Text("A THOUGHT TODAY. SOMETHING TOMORROW.", color = NoteColors.Secondary.copy(.6f),
                fontSize = 9.sp, letterSpacing = 1.1.sp, textAlign = TextAlign.Center)
        }
    }
}
