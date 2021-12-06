package com.example.astrobin.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.navigation.NavController
import com.example.astrobin.api.AstroUser
import com.example.astrobin.api.LocalAstrobinApi

@Composable
fun UserScreen(
  id: Int,
  nav: NavController
) {
  val api = LocalAstrobinApi.current
  val data = produceState<AstroUser?>(null) {
    value = api.user(id)
  }.value
  if (data == null) {
    Text("Loading...")
  } else {
    Column {
      Text(data.real_name)
      Text(data.username)
      Text("${data.following_count}")
      Text("${data.following_count}")
    }
  }
}