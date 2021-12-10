package com.example.astrobin.ui.components

import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.astrobin.ui.theme.DarkBlue
import com.example.astrobin.ui.theme.Yellow

private fun <T: Any?> T.asState(): State<T> = object : State<T> {
  override val value: T = this@asState
  override fun hashCode(): Int = this@asState.hashCode()
  override fun equals(other: Any?): Boolean {
    return this@asState?.equals(other) ?: (other == null)
  }
}

private val buttonColors = object : ButtonColors {
  val background = Color.Black.asState()
  val foreground = Color.White.asState()
  @Composable override fun backgroundColor(enabled: Boolean): State<Color> = background
  @Composable override fun contentColor(enabled: Boolean): State<Color> = foreground
}

private val selectedColors = object : ButtonColors {
  val background = Yellow.asState()
  val foreground = DarkBlue.asState()
  @Composable override fun backgroundColor(enabled: Boolean): State<Color> = background
  @Composable override fun contentColor(enabled: Boolean): State<Color> = foreground
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AstroButton(
  icon: ImageVector,
  onClick: () -> Unit,
  selected: Boolean = false,
  modifier: Modifier = Modifier,
) {
  val color = if (selected) Yellow else Color.Black
  val contentColor = if (selected) DarkBlue else Color.White
  val borderColor = if (selected) Yellow else Color.White
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(6.dp),
    border = BorderStroke(1.dp, borderColor),
    color = color,
    contentColor = contentColor,
    onClick = onClick,
  ) {
    Icon(
      icon,
      contentDescription = null,
      modifier = Modifier.padding(8.dp)
    )
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AstroButton2(
  icon: ImageVector,
  label: String,
  onClick: () -> Unit,
  selected: Boolean = false,
  modifier: Modifier = Modifier,
) {
  val color = if (selected) Yellow else Color.Transparent
  val contentColor = if (selected) DarkBlue else Yellow
  val borderColor = Yellow
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(10.dp),
    border = BorderStroke(1.dp, borderColor),
    color = color,
    contentColor = contentColor,
    onClick = onClick,
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        icon,
        contentDescription = null,
        modifier = Modifier.padding(8.dp)
      )
      Text(
        label,
        modifier = Modifier.padding(end = 8.dp)
      )
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CountButton(
  icon: ImageVector,
  label: String,
  onClick: () -> Unit,
  selected: Boolean = false,
  modifier: Modifier = Modifier,
) {
  val color = if (selected) Yellow else Color.White
  Row(
    modifier = modifier
      .clickable(onClick = onClick)
      .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      icon,
      contentDescription = null,
      tint = color,
      modifier = Modifier.padding(end = 4.dp)
    )
    Text(
      label,
      color = color,
    )
  }
}