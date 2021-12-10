package com.example.astrobin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
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
import com.example.astrobin.api.AstroImage
import com.example.astrobin.api.ImageSearchPagingSource
import com.example.astrobin.api.LocalAstrobinApi
import com.example.astrobin.ui.components.ImageRow
import com.example.astrobin.ui.components.LoadingIndicator
import com.example.astrobin.ui.components.SearchBox
import com.google.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
  nav: NavController,
  entry: NavBackStackEntry,
  padding: PaddingValues,
) {
  var searchQueryText by remember { mutableStateOf("") }
  val api = LocalAstrobinApi.current
  var pagingSource by remember { mutableStateOf<ImageSearchPagingSource?>(null) }
  val pager = remember {
    Pager(PagingConfig(pageSize = 20)) {
      ImageSearchPagingSource(
        api = api,
        params = if (searchQueryText.isBlank()) {
          emptyMap()
        } else {
          // Really want description__icontains but its broken on the API
          mapOf("title__icontains" to searchQueryText)
        }
      ).also { pagingSource = it }
    }
  }
  val keyboardController = LocalSoftwareKeyboardController.current
  val results = pager.flow.collectAsLazyPagingItems()
  val loadState = results.loadState
  LazyColumn(
    Modifier.fillMaxSize(),
    contentPadding = padding
  ) {
    item {
      Spacer(Modifier.statusBarsPadding().height(48.dp))
      SearchBox(
        modifier = Modifier.padding(horizontal = 16.dp),
        value = searchQueryText,
        onValueChange = {
          searchQueryText = it
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions {
          pagingSource?.invalidate()
          keyboardController?.hide()
        }
      )
      Spacer(Modifier.height(16.dp))
    }
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

