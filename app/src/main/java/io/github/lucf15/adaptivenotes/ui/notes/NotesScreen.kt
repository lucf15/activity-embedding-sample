package io.github.lucf15.adaptivenotes.ui.notes

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import io.github.lucf15.adaptivenotes.ui.components.NoteIcons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import io.github.lucf15.adaptivenotes.R
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus
import io.github.lucf15.adaptivenotes.ui.components.FloatingSurface
import io.github.lucf15.adaptivenotes.ui.components.StorageMessage
import io.github.lucf15.adaptivenotes.ui.theme.NoteColors

@Composable
fun NotesScreen(state: NotesContract.State, onEvent: (NotesContract.Event) -> Unit) {
    val listState = rememberLazyListState()
    Box(Modifier.fillMaxSize().background(NoteColors.Background).safeDrawingPadding()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 22.dp, bottom = 124.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item(key = "heading") {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(R.drawable.fieldnotes_mark), null, Modifier.size(36.dp))
                        Text("Fieldnotes", Modifier.padding(start = 11.dp), color = NoteColors.Text,
                            fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(30.dp))
                    Text("A little space\nto think.", color = NoteColors.Text, fontSize = 37.sp,
                        lineHeight = 43.sp, letterSpacing = (-1.5).sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(10.dp))
                    Text("Ideas, plans & things worth keeping.", color = NoteColors.Secondary,
                        fontSize = 13.sp, lineHeight = 20.sp)
                    Spacer(Modifier.height(26.dp))
                    Row(Modifier.fillMaxWidth().clip(CircleShape).background(NoteColors.Surface).padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(NoteIcons.Search, null, Modifier.size(19.dp), tint = NoteColors.Secondary)
                        BasicTextField(state.query, { onEvent(NotesContract.Event.QueryChanged(it)) },
                            Modifier.weight(1f).padding(horizontal = 10.dp, vertical = 16.dp)
                                .semantics { contentDescription = "Search notes" },
                            singleLine = true, textStyle = TextStyle(color = NoteColors.Text, fontSize = 14.sp),
                            cursorBrush = SolidColor(NoteColors.Accent),
                            decorationBox = { inner ->
                                Box {
                                    if (state.query.isEmpty()) Text("Find a thought…", color = NoteColors.Secondary, fontSize = 14.sp)
                                    inner()
                                }
                            })
                        if (state.query.isNotEmpty()) Box(Modifier.size(48.dp).clip(CircleShape)
                            .clickable { onEvent(NotesContract.Event.QueryChanged("")) }, Alignment.Center) {
                            Icon(NoteIcons.Close, "Clear search", Modifier.size(16.dp), tint = NoteColors.Secondary)
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(Modifier.fillMaxWidth().clip(CircleShape).background(NoteColors.Surface).padding(4.dp)) {
                        NotesContract.Filter.entries.forEach { filter ->
                            val selected = filter == state.filter
                            val fill by animateColorAsState(if (selected) NoteColors.SurfaceRaised else Color.Transparent, label = "filter")
                            Box(Modifier.weight(1f).heightIn(min = 48.dp).clip(CircleShape).background(fill)
                                .clickable(role = Role.Tab) { onEvent(NotesContract.Event.FilterChanged(filter)) }, Alignment.Center) {
                                Text(if (filter == NotesContract.Filter.All) "All notes" else filter.name,
                                    color = if (selected) NoteColors.Text else NoteColors.Secondary, fontSize = 12.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
                            }
                        }
                    }
                    StorageMessage(state.storage) { onEvent(NotesContract.Event.Retry) }
                    Spacer(Modifier.height(22.dp))
                    Row {
                        Text("YOUR COLLECTION", color = NoteColors.Secondary, fontSize = 10.sp,
                            letterSpacing = 1.8.sp, modifier = Modifier.weight(1f))
                        Text("${state.notes.size}", color = NoteColors.Secondary, fontSize = 11.sp)
                    }
                }
            }
            items(state.notes, key = { it.id }) { note ->
                NoteCard(note, state.selectedId == note.id) { onEvent(NotesContract.Event.OpenNote(note.id)) }
            }
            if (state.notes.isEmpty()) item {
                Text(if (state.storage == StorageStatus.Loading) "Opening your notebook…" else "No notes here yet.",
                    Modifier.padding(vertical = 26.dp), color = NoteColors.Secondary, fontSize = 15.sp)
            }
            if (state.actionFailed) item {
                Text("Couldn't create a note. Please try again.", color = NoteColors.Peach, fontSize = 13.sp)
            }
        }
        Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(120.dp)
            .background(Brush.verticalGradient(listOf(Color.Transparent, NoteColors.Background))))
        FloatingSurface(Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)) {
            Row(Modifier.clip(CircleShape).clickable(
                enabled = !state.creating && state.storage !in setOf(StorageStatus.Loading, StorageStatus.ReadError),
                role = Role.Button,
            ) { onEvent(NotesContract.Event.CreateNote) }.padding(horizontal = 24.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(NoteIcons.Add, null, Modifier.size(20.dp), tint = NoteColors.Accent)
                Text(if (state.creating) "Opening…" else "New note", color = NoteColors.Text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
