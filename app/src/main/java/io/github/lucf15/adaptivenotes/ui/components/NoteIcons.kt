package io.github.lucf15.adaptivenotes.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object NoteIcons {
    val Add = icon("Add") {
        moveTo(12f, 5f); lineTo(12f, 19f)
        moveTo(5f, 12f); lineTo(19f, 12f)
    }
    val Close = icon("Close") {
        moveTo(6f, 6f); lineTo(18f, 18f)
        moveTo(18f, 6f); lineTo(6f, 18f)
    }
    val ArrowBack = icon("Back", autoMirror = true) {
        moveTo(19f, 12f); lineTo(5f, 12f)
        moveTo(11f, 6f); lineTo(5f, 12f); lineTo(11f, 18f)
    }
    val ArrowOutward = icon("Open", autoMirror = true) {
        moveTo(6f, 18f); lineTo(18f, 6f)
        moveTo(7f, 6f); lineTo(18f, 6f); lineTo(18f, 17f)
    }
    val Check = icon("Saved") {
        moveTo(5f, 12f); lineTo(10f, 17f); lineTo(19f, 7f)
    }
    val Search = icon("Search") {
        moveTo(16f, 10f)
        arcTo(6f, 6f, 0f, true, true, 4f, 10f)
        arcTo(6f, 6f, 0f, true, true, 16f, 10f)
        moveTo(14.5f, 14.5f); lineTo(20f, 20f)
    }
    val EditNote = icon("Notebook") {
        moveTo(5f, 3f); lineTo(18f, 3f); quadTo(20f, 3f, 20f, 5f)
        lineTo(20f, 19f); quadTo(20f, 21f, 18f, 21f)
        lineTo(5f, 21f); quadTo(3f, 21f, 3f, 19f)
        lineTo(3f, 5f); quadTo(3f, 3f, 5f, 3f)
        moveTo(8f, 3f); lineTo(8f, 21f)
        moveTo(11f, 8f); lineTo(16f, 8f)
        moveTo(11f, 12f); lineTo(16f, 12f)
        moveTo(11f, 16f); lineTo(14f, 16f)
    }
    val PushPin = icon("Pin") {
        moveTo(8f, 3f); lineTo(16f, 3f)
        moveTo(9f, 3f); lineTo(9f, 10f); lineTo(6f, 14f)
        lineTo(18f, 14f); lineTo(15f, 10f); lineTo(15f, 3f)
        moveTo(12f, 14f); lineTo(12f, 21f)
    }
    val ContentCopy = icon("Copy") {
        moveTo(8f, 7f); lineTo(19f, 7f); lineTo(19f, 21f); lineTo(8f, 21f); close()
        moveTo(15f, 3f); lineTo(4f, 3f); lineTo(4f, 17f)
    }

    private fun icon(name: String, autoMirror: Boolean = false, block: PathBuilder.() -> Unit): ImageVector =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f, autoMirror = autoMirror).apply {
            path(fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 1.7f,
                strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero, pathBuilder = block)
        }.build()
}
