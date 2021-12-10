package com.example.astrobin.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
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
import com.example.astrobin.api.*
import com.example.astrobin.ui.components.LoadingIndicator
import com.example.astrobin.ui.components.UserRow
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.statusBarsPadding

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

  LazyColumn(Modifier.fillMaxSize(), contentPadding = padding) {
    if (data == null) {
      item {
        LoadingIndicator(modifier = Modifier.fillParentMaxSize())
      }
    } else {
      item {
        Image(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(data.aspectRatio),
          painter = rememberImagePainter(data.url_regular),
          contentDescription = "Full Image",
        )
      }

      item {
        Column(Modifier.padding(horizontal = 10.dp)) {
          Text(data.title ?: "", style = MaterialTheme.typography.h1)
          if (user != null) {
            UserRow(user, nav)
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
            for (subject in data.subjects) Chip(subject)
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
        .padding(top = 4.dp, end = 4.dp)
        .size(16.dp)
    )
    Text("$count", fontSize = 16.sp)
  }
}

@Composable fun Section(
  title: String,
  fullWidth: Boolean = false,
  content: @Composable () -> Unit
) {
  Column(
    Modifier
      .padding(horizontal = if (fullWidth) 0.dp else 10.dp)
      .padding(bottom = 10.dp)
  ) {
    Text(title, style = MaterialTheme.typography.h3)
    content()
  }
}

@Composable fun Chip(
  value: String,
  color: Color = Color.White,
) {
  Text(
    value,
    modifier = Modifier
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