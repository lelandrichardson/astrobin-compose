package com.example.astrobin

import android.app.Application
import android.graphics.Color
import android.view.Window
import androidx.core.view.WindowCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AstrobinApp : Application()


fun Window.makeTransparentStatusBar() {
  WindowCompat.setDecorFitsSystemWindows(this, false)
  statusBarColor = Color.TRANSPARENT
  navigationBarColor = Color.BLACK
}

fun Window.markAttributes(bits: Int, value: Boolean) {
  val params = attributes
  if (value) {
    params.flags = params.flags or bits
  } else {
    params.flags = params.flags and bits.inv()
  }
  attributes = params
}