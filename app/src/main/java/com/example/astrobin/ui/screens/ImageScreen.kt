package com.example.astrobin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.astrobin.api.*
import com.example.astrobin.exp.load
import com.example.astrobin.ui.components.*
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.time.OffsetDateTime

data class ImageModel(
  val image: AstroImageV2? = null,
  val author: AstroUserProfile? = null,
  val plateSolve: PlateSolve? = null,
  val comments: List<AstroComment>? = null,
  val commentAuthors: Map<Int, AstroUser> = emptyMap(),
) {
  companion object {
    val Empty = ImageModel()
  }
}

@Composable
fun ImageScreen(
  hash: String,
  padding: PaddingValues,
  nav: NavController
) {
  val api = LocalAstrobinApi.current
  val (model, loading) = load(ImageModel.Empty) {
    val image = api.image(hash)
    push { it.copy(image = image) }
    launch {
      val author = api.user(image.user)
      val profileId = author.userprofile ?: return@launch
      val authorProfile = api.userProfile(profileId)
      push { it.copy(author = authorProfile) }
    }
    launch {
      val plateSolve = api.plateSolve(19, image.pk)
      push { it.copy(plateSolve = plateSolve) }
    }
    launch {
      val comments = api.comments(19, image.pk)
      push { it.copy(comments = comments) }

      val commentAuthors = comments.map { it.author }.distinct().asFlow().map {
        api.user(it)
      }.toList().associateBy { it.id }

      push { it.copy(commentAuthors = commentAuthors) }
    }
  }
  ImageScreen(
    loading,
    model.image,
    model.author,
    model.plateSolve,
    model.comments,
    model.commentAuthors,
    padding,
    nav
  )
}

@Composable fun ImageScreen(
  loading: Boolean,
  image: AstroImageV2?,
  author: AstroUserProfile?,
  plateSolve: PlateSolve?,
  comments: List<AstroComment>?,
  commentAuthors: Map<Int, AstroUser>,
  padding: PaddingValues,
  nav: NavController,
) {
  var annotations by remember { mutableStateOf(false) }

  LazyColumn(Modifier.fillMaxSize(), contentPadding = padding) {
    if (image != null) {
      item {
        val regularPainter = rememberImagePainter(image.url_regular)
        val annotatedPainter = rememberImagePainter(plateSolve?.image_file)
        Box {
          Image(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(image.aspectRatio),
            painter = regularPainter,
            contentDescription = "Full Image",
          )
          if (annotations) {
            Image(
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(image.aspectRatio),
              painter = annotatedPainter,
              contentDescription = "Full Image",
            )
          }
        }
      }

      item {
        Row(
          Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          AstroButton(
            icon = Icons.Filled.Fullscreen,
            onClick = {
              val hd = image.url_hd.urlEncode()
              val solution = plateSolve?.image_file?.urlEncode() ?: ""
              val w = image.w.toString()
              val h = image.h.toString()
              nav.navigate(
                "fullscreen?hd=$hd&solution=$solution&w=$w&h=$h"
              )
            },
            modifier = Modifier.padding(end = 8.dp),
          )
          AstroButton(
            icon = Icons.Outlined.Layers,
            selected = annotations,
            onClick = { annotations = !annotations },
            modifier = Modifier.padding(end = 8.dp),
          )

          Spacer(Modifier.weight(1f))

          CountButton(
            icon = Icons.Outlined.BookmarkBorder,
            label = image.bookmarksCount.toString(),
            selected = false,
            onClick = {},
          )

          CountButton(
            icon = Icons.Outlined.ThumbUp,
            label = image.likesCount.toString(),
            selected = false,
            onClick = {},
          )
        }
      }

      item {
        Column(Modifier.padding(horizontal = 10.dp)) {
          Text(image.title ?: "", style = MaterialTheme.typography.h1)
          if (author != null) {
            Row(
              Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              UserRow(author, nav)
              AstroButton2(
                icon = Icons.Outlined.PersonAdd,
                label = "Follow",
                selected = false,
                onClick = {},
                modifier = Modifier
              )
            }
          } else {
            CircularProgressIndicator(
              Modifier
                .align(Alignment.CenterHorizontally)
            )
          }
        }
      }

      if (plateSolve != null) {
        item {
          Section("What is this") {
            FlowRow(mainAxisSpacing = 10.dp, crossAxisSpacing = 4.dp) {
              for (subject in plateSolve.objects_in_field.split(","))
                Chip(subject.trim(), onClick = { nav.navigate("search?q=${subject.trim().urlEncode()}")})
            }
          }
        }

        if (image.description != null) {
          item {
            Section("Description") {
              Text(image.description)
            }
          }
        }

        item {
          Section("Technical Card") {
            TechCardItem("Declination", plateSolve.dec)
            TechCardItem("Right Ascension", plateSolve.ra)
            TechCardItem("Data Source", image.dataSource)
            TechCardItem("Resolution", "${image.w}px x ${image.h}px")
            TechCardItem("Pixel Scale", "${plateSolve.pixscale} arc-sec/px")
            TechCardItem("Imaging Camera(s)", image
              .imagingCameras
              .joinToString(", ") { "${it.make} ${it.name}" }
            )
            TechCardItem("Imaging Telescope(s)", image
              .imagingTelescopes
              .joinToString(", ") { "${it.make} ${it.name}" }
            )
          }
        }

        item {
          Section("Sky Plot") {
            Image(
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
              painter = rememberImagePainter(plateSolve.skyplot_zoom1),
              contentScale = ContentScale.FillWidth,
              contentDescription = "Sky Plot",
            )
          }
        }
      }

      item {
        Section("Histogram") {
          Image(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(274f / 120f),
            painter = rememberImagePainter(image.url_histogram),
            contentScale = ContentScale.FillWidth,
            contentDescription = "Histogram",
          )
        }
      }
      if (comments != null) {
        item {
          Section("Comments") {}
        }
        items(comments, key = {it.id }) {
          CommentRow(it, commentAuthors[it.author])
        }
      }
    }
  }
  if (loading) {
    LoadingBar(modifier = Modifier.fillMaxWidth())
  }
}

@Composable fun CommentRow(comment: AstroComment, author: AstroUser?) {
  Row(Modifier.padding(start=50.dp * (comment.depth-1))) {
    AstroAvatar(imageUrl = comment.author_avatar)
    Column(Modifier.padding(start=8.dp)) {
      Row {
        Text(author?.username ?: "...", fontWeight = FontWeight.Bold)
        Text(timeAgo(comment.created), color=Color.Gray, modifier = Modifier.padding(start = 8.dp))
      }
      Text(comment.text, modifier=Modifier.padding(vertical=4.dp))
    }
  }
}


fun timeAgo(isoDate: String): String {
  return "5 days ago"
//  return OffsetDateTime.parse(isoDate).
}

@Composable fun IconCount(
  count: Int,
  icon: ImageVector,
  contentDescription: String? = null,
) {
  Row(Modifier.padding(end=12.dp)) {
    Icon(
      icon,
      contentDescription = contentDescription,
      modifier = Modifier
        .padding(top = 0.dp, end = 4.dp)
        .size(14.dp)
    )
    Text("$count", style = MaterialTheme.typography.subtitle2)
  }
}

@Composable fun Section(
  title: String,
  fullWidth: Boolean = false,
  content: @Composable () -> Unit
) {
  Column(
    Modifier
      .padding(horizontal = if (fullWidth) 0.dp else 16.dp)
      .padding(bottom = 16.dp)
  ) {
    Text(title, style = MaterialTheme.typography.h1, modifier = Modifier.padding(bottom = 8.dp))
    content()
  }
}

@Composable fun Chip(
  value: String,
  color: Color = Color.White,
  onClick: () -> Unit,
) {
  Text(
    value,
    modifier = Modifier
      .clickable(onClick = onClick)
      .border(1.dp, color, RoundedCornerShape(6.dp))
      .padding(4.dp, 4.dp),
    style = MaterialTheme.typography.caption,
    fontWeight = FontWeight.Bold,
    color = color,
  )
}

@Composable fun TechCardItem(
  key: String,
  value: String?,
) {
  if (value != null) {
    Row {
      Text(key, fontWeight = FontWeight.Bold)
      Text(": ")
      Text(value)
    }
  }
}

fun String.urlEncode(): String = URLEncoder.encode(this, "utf-8")