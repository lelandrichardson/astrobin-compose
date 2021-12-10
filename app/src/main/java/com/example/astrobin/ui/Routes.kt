package com.example.astrobin.ui

object Routes {
  val Top = "top"
  val Latest = "latest"
  val Search = "search"
  val Profile = "profile"
  fun User(id: Int) = "user/$id"
  fun Image(hash: String) = "image/$hash"
}