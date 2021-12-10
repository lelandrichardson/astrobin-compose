package com.example.astrobin.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingIndicator(modifier: Modifier) {
  Box(
    modifier = modifier
  ) {
    CircularProgressIndicator(
      color = Color.White,
      modifier = Modifier.align(Alignment.Center)
    )
  }
}