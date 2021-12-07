package com.example.astrobin

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
  markAttributes(
    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
    true
  )
  decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
      or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
  markAttributes(
    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
    false
  )
  statusBarColor = Color.TRANSPARENT
  navigationBarColor = Color.TRANSPARENT
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