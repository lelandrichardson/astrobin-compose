package com.example.astrobin.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ApiTest() {
  val api = LocalAstrobinApi.current
  LaunchedEffect(Unit) {
    val userId = 24520 // Min Xie
    val userProfileId = 93620 // leland
    val username = "Lrichardson"
    val imageHash = "r259z7"
    val imageId = 681332
    val imageContentType = 19
//    api.login("Lrichardson", "<redacted>")
    val a = api.user(userId)
    val b = api.userProfile(username)
    val c = api.userProfile(userProfileId)
    val d = api.image(imageId)
    val e = api.image(imageHash)
    val f = api.imageOld(imageHash)
    val g = api.plateSolve(imageContentType, imageId)
    val h = api.comments(imageContentType, imageId)
  }
}
