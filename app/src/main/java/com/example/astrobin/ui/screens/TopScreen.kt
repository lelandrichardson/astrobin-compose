package com.example.astrobin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.astrobin.api.LocalAstrobinApi
import com.example.astrobin.api.TopPick
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import com.example.astrobin.api.*
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.astrobin.ui.components.LoadingIndicator
import com.example.astrobin.ui.components.TopPickRow

@Composable
fun TopScreen(
  padding: PaddingValues,
  nav: NavController
) {
  val api = LocalAstrobinApi.current
  val pager = remember { Pager(PagingConfig(pageSize = 20)) { TopPickPagingSource(api) } }
  val topPicks = pager.flow.collectAsLazyPagingItems()
  val loadState = topPicks.loadState
  LazyColumn(Modifier.fillMaxSize(), contentPadding = padding) {
    item { Spacer(Modifier.statusBarsPadding()) }
    item {
      Text(
        "Top Picks",
        style = MaterialTheme.typography.h1,
        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
      )
    }
    items(topPicks) {
      TopPickRow(it!!, nav)
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




