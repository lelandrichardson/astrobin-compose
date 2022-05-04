package com.example.astrobin.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.astrobin.api.AstroUserProfile
import com.example.astrobin.api.avatarUrl
import com.example.astrobin.ui.screens.IconCount
import com.example.astrobin.ui.theme.Yellow

@Composable
fun UserRow(user: AstroUserProfile, nav: NavController) {
  Row(
    modifier = Modifier
      .clickable {
        nav.navigate("user/${user.id}")
      },
    verticalAlignment = Alignment.CenterVertically
  ) {
    AstroAvatar(user.url_avatar)
    Column(Modifier.padding(start=10.dp)) {
      Text(user.display_name, style= MaterialTheme.typography.subtitle1)
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

@Composable
fun SmallUserRow(
  user: String,
  likeCount: Int,
  views: Int,
  modifier: Modifier = Modifier,
) {
  Row(modifier, verticalAlignment = Alignment.CenterVertically) {
    AstroAvatar(avatarUrl(user))
    Column(Modifier.padding(start=10.dp)) {
      Text("@$user", style= MaterialTheme.typography.subtitle1)
      Row {
        IconCount(
          views,
          Icons.Outlined.Visibility,
        )
        IconCount(
          likeCount,
          Icons.Filled.ThumbUp,
        )
      }
    }
  }
}

@Composable fun AstroAvatar(
  imageUrl: String
) {
  Image(
    painter = rememberImagePainter(imageUrl),
    contentDescription = "",
    modifier = Modifier
      .border(2.dp, Yellow, CircleShape)
      .padding(2.dp)
      .clip(CircleShape)
      .background(Color.Black)
      .size(34.dp)
  )
}

@Composable fun AstroAvatar(
  user: AstroUserProfile
) {
  AstroAvatar(imageUrl = user.url_avatar)
}