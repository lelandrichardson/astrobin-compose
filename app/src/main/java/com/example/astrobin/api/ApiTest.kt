package com.example.astrobin.api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.astrobin.exp.load
import com.example.astrobin.exp.loadFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

@Composable
fun ApiTest() {
  ImageDetail2(imageHash = "r259z7")
//  val api = LocalAstrobinApi.current
//  LaunchedEffect(Unit) {
//    val userId = 24520 // Min Xie
//    val userProfileId = 93620 // leland
//    val username = "Lrichardson"
//    val imageHash = "r259z7"
//    val imageId = 681332
//    val imageContentType = 19
////    api.login("Lrichardson", "<redacted>")
//    val a = api.user(userId)
//    val b = api.userProfile(username)
//    val c = api.userProfile(userProfileId)
//    val d = api.image(imageId)
//    val e = api.image(imageHash)
//    val f = api.imageOld(imageHash)
//    val g = api.plateSolve(imageContentType, imageId)
//    val h = api.comments(imageContentType, imageId)
//  }
}

data class ImageModel(
  val image: AstroImageV2? = null,
  val author: AstroUser? = null,
  val plateSolve: PlateSolve? = null,
  val comments: List<AstroComment>? = null,
) {
  companion object {
    val Empty = ImageModel()
  }
}


@Composable fun ImageDetail(imageHash: String) {
  val api = LocalAstrobinApi.current
  val (model, loading) = load(ImageModel.Empty) {
    val image = api.image(imageHash)
    push { it.copy(image = image) }
    launch {
      val author = api.user(image.user) // -> userprofile?
      push { it.copy(author = author) }
    }
    launch {
      val plateSolve = api.plateSolve(19, image.pk)
      push { it.copy(plateSolve = plateSolve) }
    }
    launch {
      val comments = api.comments(19, image.pk)
      push { it.copy(comments = comments) }
    }
  }
  ImageDetail(loading, model.image, model.author, model.plateSolve, model.comments)
}



@Composable fun ImageDetail2(imageHash: String) {
  val api = LocalAstrobinApi.current
  val (model, loading) = loadFlow(ImageModel.Empty) {
    val image = api.image(imageHash)
    send { it.copy(image = image) }
    launch {
      val author = api.user(image.user) // -> userprofile?
      send { it.copy(author = author) }
    }
    launch {
      val plateSolve = api.plateSolve(19, image.pk)
      send { it.copy(plateSolve = plateSolve) }
    }
    launch {
      val comments = api.comments(19, image.pk)
      send { it.copy(comments = comments) }
    }
  }
  ImageDetail(loading, model.image, model.author, model.plateSolve, model.comments)
}


@Composable fun ImageDetail(
  loading: Boolean,
  image: AstroImageV2?,
  author: AstroUser?,
  plateSolve: PlateSolve?,
  comments: List<AstroComment>?,
) {
  Column(Modifier.padding(top=50.dp)) {
    Text("Loading?: $loading")
    Text("Image?: ${image?.pk}")
    Text("Author?: ${author?.username}")
    Text("PlateSolve?: ${plateSolve?.dec}")
    Text("Comments?: ${comments?.size}")
  }
}
