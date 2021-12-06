package com.example.astrobin.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.astrobin.api.ListResponse
import com.example.astrobin.api.LocalAstrobinApi
import com.example.astrobin.api.TopPick

@Composable
fun SearchScreen(
  nav: NavController,
  entry: NavBackStackEntry,
) {
  val api = LocalAstrobinApi.current
  val data = produceState<ListResponse<TopPick>?>(null) {
    value = api.topPicks(10, 0)
  }.value
  if (data == null) {
    Text(text = "Loading...")
  } else {
    Column {
      for (image in data.objects) {
        Text(image.image)
      }
    }
  }
}