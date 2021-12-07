package com.example.astrobin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.astrobin.api.AstroImage
import com.example.astrobin.api.AstroUser
import com.example.astrobin.api.LocalAstrobinApi

@Composable
fun UserScreen(
  id: Int,
  nav: NavController
) {
  val api = LocalAstrobinApi.current
  val data = produceState<AstroUser?>(null) {
    value = api.user(id)
  }.value
  UserScreenContent(data)
}

@Composable
private fun UserScreenContent(user: AstroUser?) {
  Column(
    Modifier.fillMaxSize()
  ) {
    if (user == null) {
      CircularProgressIndicator(
        Modifier
          .align(Alignment.CenterHorizontally)
      )
    } else {
        val avatarPainter =
            user.avatar?.let { rememberImagePainter(it) } ?: ColorPainter(Color.LightGray)
        Image(
            painter = avatarPainter,
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(128.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Black, CircleShape)
        )
        Text(
            text = user.real_name,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 32.sp
        )
        Text(
            text = user.username,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("${user.following_count}")
            Spacer(modifier = Modifier.width(4.dp))
            Text("${user.following_count}")
        }
        Text("${user.about}")

        // TODO: Gotta paginate!
        val api = LocalAstrobinApi.current
        val userImages = produceState(emptyList<AstroImage>()) {
            value = api.imageSearch(
                limit = 0,
                offset = 0,
                mapOf("user" to user.username)
            ).objects
        }.value
        UserImages(userImages)
    }
  }
}

@Composable
private fun UserImages(userImages: List<AstroImage>) {
  for (image in userImages) {
    Image(
      painter = rememberImagePainter(image.url_regular),
      contentDescription = image.title
    )
  }
}

@Preview
@Composable
fun UserScreenPreview() {
  val data = AstroUser(
    id = 12345,
    username = "DanyBoricua",
    real_name = "Daniel Santiago",
    followers_count = 100,
    following_count = 10,
    post_count = 5,
    received_likes_count = 1000000,
    image_count = 3,
    about = "Cool Astro Dude",
    hobbies = "Astro Gaming",
    website = null,
    job = "Sanitation Engineer",
    date_joined = "2020-12-27T05:07:46.108323",
    language = "es",
    avatar = null,
    resource_uri = "/api/v1/userprofile/12345/"
  )
  UserScreenContent(data)
}