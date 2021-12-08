package com.example.astrobin

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import coil.ImageLoader
import com.example.astrobin.api.AstrobinApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var api: AstrobinApi

  @Inject
  lateinit var imageLoader: ImageLoader

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.makeTransparentStatusBar()
    setContent { Astrobin(api, imageLoader) }
  }
}

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