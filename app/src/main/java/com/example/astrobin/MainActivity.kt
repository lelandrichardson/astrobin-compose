package com.example.astrobin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.astrobin.api.AstrobinApi
import com.example.astrobin.api.retrofit

class MainActivity : ComponentActivity() {
  private val api = retrofit.create(AstrobinApi::class.java)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { Astrobin(api) }
  }
}
