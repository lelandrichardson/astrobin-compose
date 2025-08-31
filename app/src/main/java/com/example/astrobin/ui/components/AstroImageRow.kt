package com.example.astrobin.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.astrobin.api.AstroImage
import com.example.astrobin.api.TopPickV2


private val imageForegroundGradient = Brush.linearGradient(
  0.5f to Color.Transparent,
  1.0f to Color.Black,
  start = Offset.Zero,
  end = Offset(0f, Float.POSITIVE_INFINITY)
)

private val imageRoundedCornerShape = RoundedCornerShape(8.dp)

private val imageModifier = Modifier
  .fillMaxWidth()
  .aspectRatio(16f / 9f)
  .clip(imageRoundedCornerShape)
  .background(Color.Black)
  .foreground(imageForegroundGradient)

@Composable
fun AstroImage(
  imageUrl: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Image(
    painter = rememberImagePainter(imageUrl),
    contentDescription = "",
    contentScale = ContentScale.FillWidth,
    // Bug here if I don't specify a size, I want fillWidth(). :(
    modifier = modifier
      .then(imageModifier)
      .clickable(onClick = onClick)
  )
}

@Composable
fun AstroImageWithContent(
  imageUrl: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit,
) {
  Box(modifier) {
    AstroImage(
      imageUrl,
      onClick,
    )
    content()
  }
}

@Composable
fun TopPickRow(topPick: TopPickV2, nav: NavController) {
  val image = topPick.image
  AstroImageWithContent(
    modifier = Modifier.padding(horizontal = 16.dp),
    imageUrl = image.url_regular,
    onClick = { nav.navigate("image/${image.hash}")}
  ) {
    Text(
      image.title ?: "",
      style = MaterialTheme.typography.h1,
      maxLines = 1,
      fontSize = 18.sp,
      modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
    )
    SmallUserRow(
      user = image.username,
      likeCount = image.likeCount,
      views = image.viewCount,
      modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
    )
  }
}

@Composable
fun ImageRow(image: AstroImage, nav: NavController) {
  AstroImageWithContent(
    modifier = Modifier.padding(horizontal = 16.dp),
    imageUrl = image.url_regular,
    onClick = {
      if (image.hash != null) {
        nav.navigate("image/${image.hash}")
      }
    }
  ) {
    Text(
      text = image.title ?: "",
      style = MaterialTheme.typography.h1,
      color = Color.White,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
    )
    SmallUserRow(
      user = image.user,
      likeCount = image.likes,
      views = image.views,
      modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
    )
  }
}

@Composable
fun UserImageRow(image: AstroImage, nav: NavController) {
  AstroImageWithContent(
    modifier = Modifier.padding(horizontal = 16.dp),
    imageUrl = image.url_regular,
    onClick = {
      if (image.hash != null) {
        nav.navigate("image/${image.hash}")
      }
    }
  ) {
    Text(
      text = image.title ?: "",
      style = MaterialTheme.typography.h1,
      color = Color.White,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
    )
  }
}
