package com.example.astrobin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.example.astrobin.api.AstroUserProfile
import com.example.astrobin.api.ImageSearchPagingSource
import com.example.astrobin.api.LocalAstrobinApi
import com.example.astrobin.ui.components.LoadingIndicator
import com.example.astrobin.ui.components.UserImageRow
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun UserScreen(
  id: Int,
  padding: PaddingValues,
  nav: NavController
) {
  val api = LocalAstrobinApi.current
  val user = produceState<AstroUserProfile?>(null) {
    value = api.userProfile(id)
  }.value
  if (user == null) {
    LoadingIndicator(Modifier.fillMaxWidth().statusBarsPadding())
  } else {
    val pager = remember(user.username) {
      Pager(PagingConfig(pageSize = 20)) {
        ImageSearchPagingSource(api, mapOf("user" to user.username))
      }
    }
    val userImages = pager.flow.collectAsLazyPagingItems()
    val loadState = userImages.loadState
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = padding,
    ) {
      item { Spacer(Modifier.statusBarsPadding()) }
      item {
        UserHeaderContent(user, nav)
        Spacer(modifier = Modifier.height(16.dp))
      }

      items(userImages) {
        UserImageRow(it!!, nav)
        Spacer(modifier = Modifier.height(8.dp))
      }
      when {
        loadState.refresh is LoadState.Loading -> {
          item { LoadingIndicator(Modifier.fillMaxWidth()) }
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
}

@Composable
private fun UserHeaderContent(user: AstroUserProfile, nav: NavController) {
  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    Spacer(modifier = Modifier.height(16.dp))
    Image(
      painter = rememberImagePainter(user.url_avatar),
      contentDescription = "avatar",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .size(128.dp)
        .clip(CircleShape)
        .border(2.dp, Color.Black, CircleShape)
    )
    Text(
      text = user.display_name,
      modifier = Modifier.align(Alignment.CenterHorizontally),
      fontSize = 32.sp
    )
    Text(
      text = "@${user.username}",
      modifier = Modifier.align(Alignment.CenterHorizontally),
    )
    Row(
      modifier = Modifier.align(Alignment.CenterHorizontally),
    ) {
      Icon(
        imageVector = Icons.Filled.PeopleAlt,
        contentDescription = "like icon",
        modifier = Modifier
          .size(24.dp)
          .padding(2.dp)
      )
      Text("${user.received_likes_count}")
      Spacer(modifier = Modifier.width(8.dp))
      Icon(
        imageVector = Icons.Filled.ThumbUp,
        contentDescription = "followers icon",
        modifier = Modifier
          .size(24.dp)
          .padding(2.dp)
      )
      Text("${user.followers_count}")
    }
    if (user.about != null) {
      Text("${user.about}")
    }
  }
}
