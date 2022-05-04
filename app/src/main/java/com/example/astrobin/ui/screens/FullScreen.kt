package com.example.astrobin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.astrobin.ui.components.*

@Composable
fun FullScreen(
  hd: String,
  solution: String?,
  w: Int,
  h: Int,
  padding: PaddingValues,
  nav: NavController
) {
  var annotations by remember { mutableStateOf(false) }
  var scale by remember { mutableStateOf(1f) }
  var offsetX by remember { mutableStateOf(0f) }
  var offsetY by remember { mutableStateOf(0f) }
  val aspectRatio = w.toFloat() / h.toFloat()
  val zoomModifier = Modifier.graphicsLayer {
    scaleX = scale
    scaleY = scale
    translationX = offsetX
    translationY = offsetY
  }
  Box(
    Modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        forEachGesture {
          awaitPointerEventScope {
            awaitFirstDown()
            do {
              val event = awaitPointerEvent()
              scale *= event.calculateZoom()
              val offset = event.calculatePan()
              offsetX += offset.x
              offsetY += offset.y
            } while (event.changes.any { it.pressed })
          }
        }
      }
      .background(Color.Black)
  ) {
    val regularPainter = rememberImagePainter(hd)
    Image(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(aspectRatio)
        .align(Alignment.Center)
        .then(zoomModifier),
      painter = regularPainter,
      contentDescription = "Full Image",
    )
    if (solution != null) {
      val annotatedPainter = rememberImagePainter(solution)
      if (annotations) {
        Image(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .align(Alignment.Center)
            .then(zoomModifier),
          painter = annotatedPainter,
          contentDescription = "Full Image",
        )
      }
      AstroButton(
        icon = Icons.Outlined.Layers,
        selected = annotations,
        onClick = { annotations = !annotations },
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(24.dp)
        ,
      )
    }
  }
}