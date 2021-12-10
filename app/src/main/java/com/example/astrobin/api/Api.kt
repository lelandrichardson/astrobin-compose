package com.example.astrobin.api

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

val LocalAstrobinApi = staticCompositionLocalOf<AstrobinApi> {
  error("No Astrobin API was provided, but it should have been")
}

interface AstrobinApi {
  @GET("userprofile/{id}/")
  suspend fun user(@Path("id") id: Int): AstroUser

  @GET("userprofile/")
  suspend fun user(@Query("username") username: String): ListResponse<AstroUser>

  @GET("image/{hash}/")
  suspend fun image(@Path("hash") hash: String): AstroImage

  @GET("image/")
  suspend fun imageSearch(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int,
    @QueryMap params: Map<String, String>
  ): ListResponse<AstroImage>

  @GET("toppick/")
  suspend fun topPicks(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int,
  ): ListResponse<TopPick>

  @GET("toppicknominations/")
  suspend fun topPickNominations(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int,
  ): ListResponse<TopPick>

  @GET("imageoftheday/")
  suspend fun imageOfTheDay(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int,
  ): ListResponse<TopPick>
}

data class ListResponse<T>(
  @field:Json(name="meta") val meta: AstroResultsMeta,
  @field:Json(name="objects") val objects: List<T>,
)

private fun imageUrl(hash: String, type: String) = "https://www.astrobin.com/$hash/0/rawthumb/$type/"

data class TopPick(
  @field:Json(name="date") val date: String,
  @field:Json(name="image") val image: String,
  @field:Json(name="resource_uri") val resource_uri: String
) {
  val hash: String get() = image.substringAfterLast('/')
  val url_regular: String get() = imageUrl(hash, "regular")
  val url_thumb: String get() = imageUrl(hash, "thumb")
  val url_real: String get() = imageUrl(hash, "real")
  val url_gallery: String get() = imageUrl(hash, "gallery")
  val url_hd: String get() = imageUrl(hash, "hd")
}

data class AstroImage(
  @field:Json(name="id") val id: Int,
  @field:Json(name="hash") val hash: String?,
  @field:Json(name="title") val title: String?,
  @field:Json(name="user") val user: String, // username

  // dates
  @field:Json(name="published") val published: String,
  @field:Json(name="updated") val updated: String,
  @field:Json(name="uploaded") val uploaded: String,

  // astrometry
  @field:Json(name="is_solved") val is_solved: Boolean,
  @field:Json(name="solution_status") val solution_status: String,
  @field:Json(name="ra") val ra: String?, // float
  @field:Json(name="dec") val dec: String?, // Float
  @field:Json(name="pixscale") val pixscale: String?, // Float
  @field:Json(name="radius") val radius: String?, // float
  @field:Json(name="orientation") val orientation: String?, // Float
  @field:Json(name="w") val w: Int,
  @field:Json(name="h") val h: Int,

  // images
  @field:Json(name="url_advanced_solution") val url_advanced_solution: String?,
  @field:Json(name="url_duckduckgo") val url_duckduckgo: String,
  @field:Json(name="url_duckduckgo_small") val url_duckduckgo_small: String,
  @field:Json(name="url_gallery") val url_gallery: String,
  @field:Json(name="url_hd") val url_hd: String,
  @field:Json(name="url_histogram") val url_histogram: String,
  @field:Json(name="url_real") val url_real: String,
  @field:Json(name="url_regular") val url_regular: String,
  @field:Json(name="url_skyplot") val url_skyplot: String?,
  @field:Json(name="url_solution") val url_solution: String?,
  @field:Json(name="url_thumb") val url_thumb: String,

  // statistics
  @field:Json(name="comments") val comments: Int,
  @field:Json(name="likes") val likes: Int,
  @field:Json(name="views") val views: Int,

  // technical card
  @field:Json(name="imaging_cameras") val imaging_cameras: List<String>,
  @field:Json(name="imaging_telescopes") val imaging_telescopes: List<String>,
  @field:Json(name="data_source") val data_source: String,
  @field:Json(name="locations") val locations: List<String>,
  @field:Json(name="remote_source") val remote_source: String?,
  @field:Json(name="subjects") val subjects: List<String>,

  // other
  @field:Json(name="animated") val animated: Boolean,
  @field:Json(name="bookmarks") val bookmarks: Int,
  @field:Json(name="is_final") val is_final: Boolean,
  @field:Json(name="license") val license: Int,
  @field:Json(name="license_name") val license_name: String,
  @field:Json(name="link") val link: String?,
  @field:Json(name="link_to_fits") val link_to_fits: String?,
  @field:Json(name="resource_uri") val resource_uri: String,
  @field:Json(name="revisions") val revisions: List<String>,
) {
  val aspectRatio: Float get() = w.toFloat() / h.toFloat()
}

data class AstroUser(
  @field:Json(name="id") val id: Int,
  @field:Json(name="username") val username: String,
  @field:Json(name="real_name") val real_name: String?,

  // stats
  @field:Json(name="followers_count") val followers_count: Int,
  @field:Json(name="following_count") val following_count: Int,
  @field:Json(name="post_count") val post_count: Int,
  @field:Json(name="received_likes_count") val received_likes_count: Int,
  @field:Json(name="image_count") val image_count: Int,

  // bio
  @field:Json(name="about") val about: String?,
  @field:Json(name="hobbies") val hobbies: String?,
  @field:Json(name="website") val website: String?,
  @field:Json(name="job") val job: String?,

  @field:Json(name="date_joined") val date_joined: String,
  @field:Json(name="language") val language: String,
  @field:Json(name="avatar") val avatar: String?,
  @field:Json(name="resource_uri") val resource_uri: String,
) {
  val display_name: String get() = real_name ?: "@$username"
  val url_avatar: String get() {
    // since we are faking some avatar data temporarily, i am special casing my avatar for demo purposes
    if (id == 93620) return "https://cdn.astrobin.com/images/avatars/2/7/93669/resized/194/65616d9d63bbe81142157196d34396f4.png"
    return avatar ?: avatarUrl(username)
  }
}

fun avatarUrl(username: String): String {
  return "https://i.pravatar.cc/300?u=$username"
}

data class AstroResultsMeta(
  @field:Json(name="limit") val limit: Int,
  @field:Json(name="next") val next: String?,
  @field:Json(name="offset") val offset: Int,
  @field:Json(name="previous") val previous: String?,
  @field:Json(name="total_count") val total_count: Int,
)
