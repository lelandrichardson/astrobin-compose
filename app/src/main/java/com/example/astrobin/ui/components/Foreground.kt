package com.example.astrobin.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.LayoutDirection


fun Modifier.foreground(
  brush: Brush,
  shape: Shape = RectangleShape,
  /*@FloatRange(from = 0.0, to = 1.0)*/
  alpha: Float = 1.0f
) = this.then(
  Foreground(
    brush = brush,
    alpha = alpha,
    shape = shape,
    inspectorInfo = debugInspectorInfo {
      name = "foreground"
      properties["alpha"] = alpha
      properties["brush"] = brush
      properties["shape"] = shape
    }
  )
)

private class Foreground constructor(
  private val color: Color? = null,
  private val brush: Brush? = null,
  private val alpha: Float = 1.0f,
  private val shape: Shape,
  inspectorInfo: InspectorInfo.() -> Unit
) : DrawModifier, InspectorValueInfo(inspectorInfo) {

  // naive cache outline calculation if size is the same
  private var lastSize: Size? = null
  private var lastLayoutDirection: LayoutDirection? = null
  private var lastOutline: Outline? = null

  override fun ContentDrawScope.draw() {
    drawContent()
    if (shape === RectangleShape) {
      // shortcut to avoid Outline calculation and allocation
      drawRect()
    } else {
      drawOutline()
    }
  }

  private fun ContentDrawScope.drawRect() {
    color?.let { drawRect(color = it) }
    brush?.let { drawRect(brush = it, alpha = alpha) }
  }

  private fun ContentDrawScope.drawOutline() {
    val outline =
      if (size == lastSize && layoutDirection == lastLayoutDirection) {
        lastOutline!!
      } else {
        shape.createOutline(size, layoutDirection, this)
      }
    color?.let { drawOutline(outline, color = color) }
    brush?.let { drawOutline(outline, brush = brush, alpha = alpha) }
    lastOutline = outline
    lastSize = size
  }

  override fun hashCode(): Int {
    var result = color?.hashCode() ?: 0
    result = 31 * result + (brush?.hashCode() ?: 0)
    result = 31 * result + alpha.hashCode()
    result = 31 * result + shape.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    val otherModifier = other as? Foreground ?: return false
    return color == otherModifier.color &&
        brush == otherModifier.brush &&
        alpha == otherModifier.alpha &&
        shape == otherModifier.shape
  }

  override fun toString(): String =
    "Background(color=$color, brush=$brush, alpha = $alpha, shape=$shape)"
}