package com.example.astrobin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.example.astrobin.api.*
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun SearchScreen(
  nav: NavController,
  entry: NavBackStackEntry,
  padding: PaddingValues,
) {
  val api = LocalAstrobinApi.current
  val pager = remember { Pager(PagingConfig(pageSize = 20)) { ImageSearchPagingSource(api, emptyMap()) } }
  val results = pager.flow.collectAsLazyPagingItems()
  val loadState = results.loadState
  LazyColumn(Modifier.fillMaxSize(), contentPadding = padding) {
    item { Spacer(Modifier.statusBarsPadding()) }
    item { Text("Image Search", style = MaterialTheme.typography.h1) }
    items(results) {
      ImageRow(it!!, nav)
      Spacer(Modifier.height(8.dp))
    }
    when {
      loadState.refresh is LoadState.Loading -> {
        item { LoadingIndicator(Modifier.fillParentMaxSize()) }
      }
      loadState.append is LoadState.Loading -> {
        item { LoadingIndicator(Modifier.fillMaxWidth()) }
      }
      loadState.refresh is LoadState.Error -> {
        val e = loadState.refresh as LoadState.Error
        item {
          // TODO: retry?
          Text(
            text = e.error.localizedMessage!!,
            modifier = Modifier.fillParentMaxSize(),
          )
        }
      }
      loadState.append is LoadState.Error -> {
        val e = loadState.append as LoadState.Error
        item {
          // TODO: retry?
          Text(e.error.localizedMessage!!)
        }
      }
    }
  }
}

@Composable
private fun ImageRow(image: AstroImage, nav: NavController) {
  Box {
    Image(
      painter = rememberImagePainter(image.url_regular),
      contentDescription = image.title,
      contentScale = ContentScale.FillWidth,
      // Bug here if I don't specify a size, I want fillWidth(). :(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f)
        .border(2.dp, Color.DarkGray)
        .clickable {
          nav.navigate("image/${image.hash}")
        },
    )
    Text(
      text = image.title,
      color = Color.White,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.align(Alignment.BottomCenter),
    )
  }
}