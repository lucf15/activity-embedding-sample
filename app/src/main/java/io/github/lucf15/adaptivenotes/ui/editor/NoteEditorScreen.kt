package io.github.lucf15.adaptivenotes.ui.editor

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import io.github.lucf15.adaptivenotes.ui.components.NoteIcons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lucf15.adaptivenotes.domain.model.NoteCategory
import io.github.lucf15.adaptivenotes.domain.model.StorageStatus
import io.github.lucf15.adaptivenotes.ui.components.FloatingSurface
import io.github.lucf15.adaptivenotes.ui.components.RoundIcon
import io.github.lucf15.adaptivenotes.ui.components.StorageMessage
import io.github.lucf15.adaptivenotes.ui.theme.NoteColors

@Composable
fun NoteEditorScreen(
    state: NoteEditorContract.State,
    onEvent: (NoteEditorContract.Event) -> Unit,
    onClose: () -> Unit,
    focusTitle: Boolean,
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val scroll = androidx.compose.runtime.key(state.noteId) { rememberScrollState() }
    val wordCount = remember(state.body.text) { state.body.text.trim().split(Regex("\\s+")).count { it.isNotBlank() } }
    LaunchedEffect(state.noteId, state.loading) {
        if (focusTitle && !state.loading && !state.missing && state.title.text.isEmpty()) focusRequester.requestFocus()
    }
    Box(Modifier.fillMaxSize().background(NoteColors.Background).safeDrawingPadding().imePadding()) {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically) {
                RoundIcon(NoteIcons.ArrowBack, "Back to notes", onClose)
                Column(Modifier.weight(1f).padding(start = 14.dp)) {
                    Text("YOUR NOTE", color = NoteColors.Secondary, fontSize = 10.sp, letterSpacing = 1.8.sp)
                    Spacer(Modifier.height(5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (state.storage == StorageStatus.Saved) Icon(NoteIcons.Check, null, Modifier.size(13.dp), tint = NoteColors.Green)
                        Text(when (state.storage) {
                            StorageStatus.Saved -> "Saved on this device"
                            StorageStatus.Saving -> "Saving…"
                            StorageStatus.Loading -> "Opening…"
                            else -> "Not saved yet"
                        }, Modifier.padding(start = 4.dp), color = NoteColors.Secondary, fontSize = 11.sp)
                    }
                }
            }
            if (state.loading || state.missing) {
                Column(Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center) {
                    Text(if (state.loading) "Opening your note…" else "This note isn't available.",
                        color = NoteColors.Text, fontSize = 23.sp, lineHeight = 30.sp)
                    StorageMessage(state.storage) { onEvent(NoteEditorContract.Event.Retry) }
                }
            } else {
                Column(Modifier.weight(1f).widthIn(max = 720.dp).fillMaxWidth().align(Alignment.CenterHorizontally)
                    .verticalScroll(scroll).padding(horizontal = 32.dp)
                    .padding(top = 20.dp, bottom = 130.dp)) {
                    val accent = when (state.category) {
                        NoteCategory.Ideas -> NoteColors.Accent
                        NoteCategory.Work -> NoteColors.Peach
                        NoteCategory.Personal -> NoteColors.Green
                    }
                    Text(state.category.name.uppercase(), Modifier.clip(CircleShape).background(accent.copy(.1f))
                        .padding(horizontal = 13.dp, vertical = 8.dp), color = accent, fontSize = 10.sp,
                        letterSpacing = 1.5.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(24.dp))
                    BasicTextField(state.title, { onEvent(NoteEditorContract.Event.TitleChanged(it)) },
                        Modifier.fillMaxWidth().focusRequester(focusRequester).semantics { contentDescription = "Note title" },
                        textStyle = TextStyle(color = NoteColors.Text, fontSize = 38.sp, lineHeight = 45.sp,
                            fontWeight = FontWeight.SemiBold, letterSpacing = (-1.2).sp),
                        cursorBrush = SolidColor(NoteColors.Accent),
                        decorationBox = { inner ->
                            Box {
                                if (state.title.text.isEmpty()) Text("Untitled note", color = NoteColors.Secondary.copy(.6f),
                                    fontSize = 38.sp, lineHeight = 45.sp, letterSpacing = (-1.2).sp)
                                inner()
                            }
                        })
                    Spacer(Modifier.height(23.dp))
                    Box(Modifier.width(40.dp).height(2.dp).background(accent.copy(.65f)))
                    Spacer(Modifier.height(26.dp))
                    BasicTextField(state.body, { onEvent(NoteEditorContract.Event.BodyChanged(it)) },
                        Modifier.fillMaxWidth().heightIn(min = 240.dp).semantics { contentDescription = "Note text" },
                        textStyle = TextStyle(color = NoteColors.Body, fontSize = 17.sp, lineHeight = 29.sp),
                        cursorBrush = SolidColor(NoteColors.Accent),
                        decorationBox = { inner ->
                            Box {
                                if (state.body.text.isEmpty()) Text("What's on your mind?", color = NoteColors.Secondary.copy(.6f),
                                    fontSize = 17.sp, lineHeight = 29.sp)
                                inner()
                            }
                        })
                    StorageMessage(state.storage) { onEvent(NoteEditorContract.Event.Retry) }
                }
            }
        }
        if (!state.loading && !state.missing) {
            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(112.dp)
                .background(Brush.verticalGradient(listOf(Color.Transparent, NoteColors.Background))))
            FloatingSurface(Modifier.align(Alignment.BottomCenter).padding(bottom = 22.dp)) {
                Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(48.dp).clip(CircleShape).clickable(role = Role.Button) {
                        onEvent(NoteEditorContract.Event.TogglePinned)
                    }, Alignment.Center) {
                        Icon(NoteIcons.PushPin, if (state.pinned) "Unpin note" else "Pin note", Modifier.size(20.dp),
                            tint = if (state.pinned) NoteColors.Accent else NoteColors.Secondary)
                    }
                    Text("$wordCount ${if (wordCount == 1) "word" else "words"}", Modifier.padding(horizontal = 14.dp),
                        color = NoteColors.Secondary, fontSize = 12.sp)
                    Box(Modifier.size(48.dp).clip(CircleShape).clickable(role = Role.Button) {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText(state.title.text, "${state.title.text}\n\n${state.body.text}"))
                        if (android.os.Build.VERSION.SDK_INT <= 32) Toast.makeText(context, "Note copied", Toast.LENGTH_SHORT).show()
                    }, Alignment.Center) {
                        Icon(NoteIcons.ContentCopy, "Copy note", Modifier.size(19.dp), tint = NoteColors.Secondary)
                    }
                }
            }
        }
    }
}
