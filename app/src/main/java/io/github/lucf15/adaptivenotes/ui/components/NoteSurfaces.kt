package io.github.lucf15.adaptivenotes.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.lucf15.adaptivenotes.ui.theme.NoteColors

@Composable
fun SoftCard(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) .98f else 1f, label = "card-press")
    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier.graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(shape)
            .background(Brush.verticalGradient(if (selected)
                listOf(NoteColors.SelectedTop, NoteColors.SelectedBottom)
            else listOf(NoteColors.CardTop, NoteColors.Surface)))
            .border(1.dp, if (selected) NoteColors.Accent.copy(.65f) else NoteColors.Border.copy(.65f), shape)
            .clickable(interactionSource = interaction, indication = null, role = Role.Button, onClick = onClick)
            .padding(20.dp),
        content = content,
    )
}

@Composable
fun FloatingSurface(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier.shadow(12.dp, CircleShape, ambientColor = NoteColors.Shadow, spotColor = NoteColors.Shadow)
            .clip(CircleShape)
            .background(Brush.verticalGradient(listOf(NoteColors.FloatingTop, NoteColors.Background)))
            .border(1.dp, Brush.verticalGradient(listOf(Color.White, NoteColors.Border.copy(.7f))), CircleShape),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

@Composable
fun RoundIcon(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier, tint: Color = NoteColors.Text) {
    Box(modifier.size(48.dp).clip(CircleShape).background(NoteColors.SurfaceRaised)
        .clickable(role = Role.Button, onClickLabel = label, onClick = onClick), contentAlignment = Alignment.Center) {
        Icon(icon, label, Modifier.size(21.dp), tint = tint)
    }
}
