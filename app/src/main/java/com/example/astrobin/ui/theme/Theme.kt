package com.example.astrobin.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
  primary = DarkBlue,
  primaryVariant = Color.Black,
  onPrimary = Yellow,

  secondary = Maroon,
  secondaryVariant = Maroon,
  onSecondary = Color.White,

  background = Orange,
  onBackground = Color.White,

  surface = Color(0xFF121212),
  onSurface = Color.White,

  error = Color(0xFFCF6679),
  onError = Color.Black
)

@Composable
fun AstrobinTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colors = DarkColorPalette,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}