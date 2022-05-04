package com.example.astrobin.api

import com.squareup.moshi.Json

data class AstroComment(
  @field:Json(name="id") val id: Int,
  @field:Json(name="content_type") val content_type: Int,
  @field:Json(name="object_id") val object_id: Int,
  @field:Json(name="author") val author: Int,
  @field:Json(name="author_avatar") val author_avatar: String,
  @field:Json(name="text") val text: String,
  @field:Json(name="html") val html: String,
  @field:Json(name="created") val created: String,
  @field:Json(name="updated") val updated: String,
  @field:Json(name="parent") val parent: Int?,
  @field:Json(name="deleted") val deleted: Boolean,
  @field:Json(name="pending_moderation") val pending_moderation: Boolean?,
  @field:Json(name="moderator") val moderator: Int?,
  @field:Json(name="likes") val likes: List<Int>?,
  @field:Json(name="depth") val depth: Int,
)