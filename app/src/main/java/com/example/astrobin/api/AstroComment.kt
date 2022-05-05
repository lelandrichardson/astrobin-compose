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
) {
  private val mutableChildren = mutableListOf<AstroComment>()
  val children: List<AstroComment> get() = mutableChildren

  companion object {
    fun collect(comments: List<AstroComment>): List<AstroComment> {
      if (comments.isEmpty()) return comments
      val commentById = comments.associateBy { it.id }
      val result = mutableListOf<AstroComment>()

      // !assumes comments are sorted by creation date
      for (comment in comments) {
        if (comment.parent == null) {
          result.add(comment)
        } else {
          commentById[comment.parent]!!.mutableChildren.add(comment)
        }
      }

      return result
    }
  }
}

fun <T> List<T>.deepFlattenInto(target: MutableList<T>, fn: (T) -> List<T>): List<T> {
  forEach {
    target.add(it)
    fn(it).deepFlattenInto(target, fn)
  }
  return target
}