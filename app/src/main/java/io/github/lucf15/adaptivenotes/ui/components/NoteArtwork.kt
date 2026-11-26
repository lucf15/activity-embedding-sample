package io.github.lucf15.adaptivenotes.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import io.github.lucf15.adaptivenotes.ui.theme.NoteColors

@Composable
fun NoteArtwork(modifier: Modifier = Modifier, color: Color = NoteColors.Accent) {
    Canvas(modifier) {
        val unit = size.minDimension / 100f
        val center = center
        drawCircle(Brush.radialGradient(listOf(color.copy(.18f), color.copy(0f)), center, unit * 49), unit * 49)
        rotate(-12f) {
            drawRoundRect(color.copy(.14f), Offset(center.x - 29 * unit, center.y - 35 * unit),
                Size(57 * unit, 72 * unit), CornerRadius(9 * unit))
            drawRoundRect(color.copy(.7f), Offset(center.x - 29 * unit, center.y - 35 * unit),
                Size(57 * unit, 72 * unit), CornerRadius(9 * unit), style = Stroke(1.2f * unit))
            drawLine(color.copy(.5f), Offset(center.x - 17 * unit, center.y - 34 * unit),
                Offset(center.x - 17 * unit, center.y + 35 * unit), unit)
            repeat(4) { index ->
                drawLine(color.copy(.8f), Offset(center.x - 8 * unit, center.y - 16 * unit + index * 11 * unit),
                    Offset(center.x + (if (index == 3) 7 else 17) * unit, center.y - 16 * unit + index * 11 * unit), unit * 1.6f)
            }
        }
        val star = Offset(center.x + 33 * unit, center.y - 33 * unit)
        drawLine(color, star - Offset(0f, 7 * unit), star + Offset(0f, 7 * unit), unit * 1.5f)
        drawLine(color, star - Offset(7 * unit, 0f), star + Offset(7 * unit, 0f), unit * 1.5f)
        drawCircle(color.copy(.7f), 2 * unit, Offset(center.x - 37 * unit, center.y + 28 * unit))
    }
}
