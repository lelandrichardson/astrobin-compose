package com.example.astrobin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.astrobin.api.AstroImage
import com.example.astrobin.api.ListResponse
import com.example.astrobin.api.LocalAstrobinApi
import com.example.astrobin.api.TopPick
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun TopScreen(
  padding: PaddingValues,
  nav: NavController
) {
  val api = LocalAstrobinApi.current
  val data = produceState<ListResponse<TopPick>?>(null) {
    value = api.topPicks(10, 0)
  }.value
  if (data == null) {
    Column(
      modifier = Modifier.fillMaxWidth()
    ) {
      Spacer(Modifier.statusBarsPadding().height(16.dp))
      CircularProgressIndicator(
        color = Color.White,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
    }
  } else {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = padding
    ) {
      item { Spacer(Modifier.statusBarsPadding()) }
      item { Text("Top Picks", style = MaterialTheme.typography.h1) }
      items(data.objects) {
        TopPickRow(it, nav)
        Spacer(Modifier.height(8.dp))
      }
    }
  }
}


@Composable fun TopPickRow(image: TopPick, nav: NavController) {
  Column(Modifier.fillMaxWidth()) {
    Image(
      painter = rememberImagePainter(image.url_regular),
      contentDescription = "",
      contentScale = ContentScale.FillWidth,
      // Bug here if I don't specify a size, I want fillWidth(). :(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f)
        .align(Alignment.CenterHorizontally)
        .border(2.dp, Color.DarkGray)
        .clickable {
          nav.navigate("image/${image.hash}")
        },
    )
  }
}
