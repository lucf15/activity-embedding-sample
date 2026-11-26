package io.github.lucf15.adaptivenotes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object NoteColors {
    val Background = Color(0xFFE5E7F2)
    val Surface = Color(0xFFF3F3F9)
    val SurfaceRaised = Color(0xFFD6DAEA)
    val Text = Color(0xFF192434)
    val Body = Color(0xFF3B465A)
    val Secondary = Color(0xFF566276)
    val Border = Color(0xFFB4BCD1)
    val Accent = Color(0xFF6E4DB9)
    val Primary = Color(0xFF066D73)
    val Green = Color(0xFF066F64)
    val Peach = Color(0xFF984828)
    val Error = Color(0xFFA92F4C)
    val CardTop = Color(0xFFF8F7FC)
    val SelectedTop = Color(0xFFE6DFF5)
    val SelectedBottom = Color(0xFFDCD5EE)
    val FloatingTop = Color(0xFFFAF9FD)
    val Shadow = Color(0xFF687399)
    val PlaceholderGlow = Color(0xFFDCD7EF)
}

@Composable
fun FieldnotesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = NoteColors.Primary, onPrimary = Color.White,
            secondary = NoteColors.Accent, onSecondary = Color.White,
            background = NoteColors.Background, onBackground = NoteColors.Text,
            surface = NoteColors.Surface, onSurface = NoteColors.Text,
            surfaceVariant = NoteColors.SurfaceRaised, onSurfaceVariant = NoteColors.Secondary,
            surfaceContainer = NoteColors.SurfaceRaised, outline = NoteColors.Border,
            error = NoteColors.Error, onError = Color.White,
        ),
        content = content,
    )
}
