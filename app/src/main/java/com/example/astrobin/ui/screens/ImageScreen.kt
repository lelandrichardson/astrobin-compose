package com.example.astrobin.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.ThumbUp
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
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun ImageScreen(
  hash: String,
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

  Column(
    Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    if (data == null) {
      CircularProgressIndicator(
        Modifier
          .align(Alignment.CenterHorizontally)
      )
    } else {
      Image(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(data.aspectRatio),
        painter = rememberImagePainter(data.url_regular),
        contentDescription = "Full Image",
      )
      Column(Modifier.padding(horizontal = 10.dp)) {

        Text(data.title, style = MaterialTheme.typography.h1)
        if (user != null) {
          UserRow(user, nav)
        } else {
          CircularProgressIndicator(
            Modifier
              .align(Alignment.CenterHorizontally)
          )
        }
      }

      Section("Subjects") {
        FlowRow(mainAxisSpacing = 10.dp, crossAxisSpacing = 4.dp) {
          for (subject in data.subjects) Chip(subject)
        }
      }

      Section("Technical Card") {
        TechCardItem("Declination", data.dec)
        TechCardItem("Right Ascension", data.ra)
        TechCardItem("Data Source", data.data_source)
        TechCardItem("Resolution", "${data.w}px x ${data.h}px")
        TechCardItem("Pixel Scale", "${data.pixscale} arc-sec/px")
        TechCardItem("Imaging Camera(s)", data.imaging_cameras.joinToString(", "))
        TechCardItem("Imaging Telescope(s)", data.imaging_telescopes.joinToString(", "))
      }

      Section("Description") {

      }
      // description?

      if (data.url_skyplot != null) {
        Section("Sky Plot") {
          Image(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            painter = rememberImagePainter(data.url_skyplot),
            contentScale = ContentScale.FillWidth,
            contentDescription = "Sky Plot",
          )
        }
      }

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

      Section("Nearby Images") {
//        val nearby = produceState<ListResponse<AstroImage>?>(initialValue = null) {
//          value = api.nearby(
//            data.dec.toFloat(),
//            data.ra.toFloat(),
//            data.radius.toFloat(),
//            limit = 10,
//            offset = 0,
//          )
//        }.value
//        if (nearby != null) {
//          Column {
//            for (image in nearby.objects) {
//              Text(image.resource_uri)
//            }
//          }
//        }
      }
    }
  }
}

suspend fun AstrobinApi.nearby(
  dec: Float,
  ra: Float,
  radius: Float,
  limit: Int,
  offset: Int,
): ListResponse<AstroImage> {
  return imageSearch(
    limit,
    offset,
    mapOf(
      "ra__lt" to "${ra + radius}",
      "ra__gt" to "${ra - radius}",
      "dec__lt" to "${dec + radius}",
      "dec__gt" to "${dec - radius}",
    )
  )
}

@Composable fun ImageCarousel() {

}

@Composable fun UserRow(user: AstroUser, nav: NavController) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable {
        nav.navigate("user/${user.id}")
      }
  ) {
    Box(
      Modifier
        .border(4.dp, MaterialTheme.colors.primary, CircleShape)
        .padding(8.dp)
        .background(Color.Black, CircleShape)
        .size(28.dp)
    )
    Column(Modifier.padding(start=10.dp)) {
      Text(user.display_name, style=MaterialTheme.typography.subtitle1)
      Row {
        IconCount(
          user.followers_count,
          Icons.Filled.PeopleAlt,
        )
        IconCount(
          user.received_likes_count,
          Icons.Filled.ThumbUp,
        )
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
        .padding(end = 6.dp)
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
  color: Color = MaterialTheme.colors.primary,
  contentColor: Color = contentColorFor(color)
) {
  Text(
    value,
    modifier = Modifier
      .background(color, RoundedCornerShape(4.dp))
      .padding(10.dp, 4.dp),
    style = MaterialTheme.typography.caption,
    fontWeight = FontWeight.Bold,
    color = contentColor,
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