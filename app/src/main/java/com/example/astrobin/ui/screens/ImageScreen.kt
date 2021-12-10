package com.example.astrobin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.astrobin.api.AstroImage
import com.example.astrobin.api.AstroUser
import com.example.astrobin.api.LocalAstrobinApi
import com.example.astrobin.ui.components.*
import com.google.accompanist.flowlayout.FlowRow
import java.net.URLEncoder

@Composable
fun ImageScreen(
  hash: String,
  padding: PaddingValues,
  nav: NavController
) {
  val api = LocalAstrobinApi.current
  val data = produceState<AstroImage?>(null) {
    value = api.image(hash)
  }.value
  val user = produceState<AstroUser?>(initialValue = null, data?.user) {
    val username = data?.user
    if (username != null) {
      value = api.user(username).objects.firstOrNull()
    }
  }.value

  var annotations by remember { mutableStateOf(false) }

  LazyColumn(Modifier.fillMaxSize(), contentPadding = padding) {
    if (data == null) {
      item {
        LoadingIndicator(modifier = Modifier.fillParentMaxSize())
      }
    } else {
      item {
        val regularPainter = rememberImagePainter(data.url_regular)
        val annotatedPainter = rememberImagePainter(data.url_solution)
        Box {
          Image(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(data.aspectRatio),
            painter = regularPainter,
            contentDescription = "Full Image",
          )
          if (annotations) {
            Image(
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(data.aspectRatio),
              painter = annotatedPainter,
              contentDescription = "Full Image",
            )
          }
        }
      }

      item {
        Row(Modifier
          .background(Color.Black)
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          AstroButton(
            icon = Icons.Filled.Fullscreen,
            onClick = {
              val hd = data.url_hd.urlEncode()
              val solution = data.url_solution?.urlEncode() ?: ""
              val w = data.w.toString()
              val h = data.h.toString()
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
            label = data.bookmarks.toString(),
            selected = false,
            onClick = {},
          )

          CountButton(
            icon = Icons.Outlined.ThumbUp,
            label = data.likes.toString(),
            selected = false,
            onClick = {},
          )
        }
      }

      item {
        Column(Modifier.padding(horizontal = 10.dp)) {
          Text(data.title ?: "", style = MaterialTheme.typography.h1)
          if (user != null) {
            Row(
              Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              UserRow(user, nav)
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

      item {
        Section("Subjects") {
          FlowRow(mainAxisSpacing = 10.dp, crossAxisSpacing = 4.dp) {
            for (subject in data.subjects)
              Chip(subject, onClick = { nav.navigate("search?q=${subject.urlEncode()}")})
          }
        }
      }

      item {
        Section("Technical Card") {
          TechCardItem("Declination", data.dec)
          TechCardItem("Right Ascension", data.ra)
          TechCardItem("Data Source", data.data_source)
          TechCardItem("Resolution", "${data.w}px x ${data.h}px")
          TechCardItem("Pixel Scale", "${data.pixscale} arc-sec/px")
          TechCardItem("Imaging Camera(s)", data.imaging_cameras.joinToString(", "))
          TechCardItem("Imaging Telescope(s)", data.imaging_telescopes.joinToString(", "))
        }
      }

      if (data.url_skyplot != null) {
        item {
          Section("Sky Plot") {
            Image(
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
              painter = rememberImagePainter(data.url_skyplot),
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
            painter = rememberImagePainter(data.url_histogram),
            contentScale = ContentScale.FillWidth,
            contentDescription = "Histogram",
          )
        }
      }
    }
  }
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