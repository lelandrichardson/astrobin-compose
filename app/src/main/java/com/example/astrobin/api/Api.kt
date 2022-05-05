package com.example.astrobin.api

import androidx.compose.runtime.staticCompositionLocalOf
import retrofit2.http.*

val LocalAstrobinApi = staticCompositionLocalOf<Astrobin> {
  error("No Astrobin API was provided, but it should have been")
}

class Astrobin(
  private val auth: AuthenticationInterceptor,
  private val api: AstrobinApi,
) {
  class NotFoundException : Exception()
  private fun NotFound(): Nothing { throw NotFoundException() }

  fun isLoggedIn(): Boolean = auth.isLoggedIn()

  fun logout(): Unit = auth.clear()

  suspend fun login(username: String, password: String) = auth.setCredentials(username, password)

  suspend fun register(
    username: String,
    email: String,
    password: String
  ): Boolean {
    // TODO: an api doesn't exist for this yet
    return false
  }

  suspend fun currentUser(): AstroUserProfile? = if (isLoggedIn()) api.currentUser().singleOrNull() else null

  suspend fun image(hash: String): AstroImageV2 {
    val pages = api.image(hash)
    if (pages.results.isEmpty()) NotFound()
    return pages.results.single()
  }

  suspend fun image(id: Int): AstroImageV2 = api.image(id)

  suspend fun imageRevision(image: Int): Paginated<AstroImageRevision> = api.imageRevision(image)

  suspend fun thumbnailGroup(imageId: Int): ThumbnailGroup = api.thumbnailGroup(imageId)

  suspend fun comments(
    contentType: Int,
    objectId: Int,
  ): List<AstroComment> {
    return AstroComment
      .collect(api.comments(contentType, objectId))
      .deepFlattenInto(mutableListOf()) { it.children }
  }

  suspend fun createComment(
    comment: AstroComment,
  ): AstroComment = api.createComment(comment)

  suspend fun plateSolve(
    contentType: Int,
    objectId: Int
  ): PlateSolve? = api.plateSolve(contentType, objectId).singleOrNull()

  suspend fun plateSolves(
    contentType: Int,
    objectIds: List<Int>,
  ): List<PlateSolve> = api.plateSolves(contentType, objectIds.joinToString(","))

  suspend fun user(
    id: Int
  ): AstroUser = api.user(id)

  suspend fun userProfile(id: Int): AstroUserProfile = api.userProfile(id)

  suspend fun userProfile(username: String): ListResponse<AstroUserProfile> = api.userProfile(username)

  suspend fun imageOld(hash: String): AstroImage = api.imageOld(hash)

  suspend fun imageSearch(
    limit: Int,
    offset: Int,
    params: Map<String, String>
  ): ListResponse<AstroImage> = api.imageSearch(limit, offset, params)

  suspend fun topPicks(
    limit: Int,
    offset: Int,
  ): ListResponse<TopPick> = api.topPicks(limit, offset)

  suspend fun topPickNominations(
    limit: Int,
    offset: Int,
  ): ListResponse<TopPick> = api.topPickNominations(limit, offset)

  suspend fun imageOfTheDay(
    limit: Int,
    offset: Int,
  ): ListResponse<TopPick> = api.imageOfTheDay(limit, offset)
}

interface AstrobinApi {

  @GET("api/v2/common/userprofiles/current/")
  suspend fun currentUser(): List<AstroUserProfile>

  @GET("api/v2/images/image/")
  suspend fun image(@Query("hash") hash: String): Paginated<AstroImageV2>

  @GET("api/v2/images/image/{id}/")
  suspend fun image(@Path("id") id: Int): AstroImageV2

  @GET("api/v2/images/image-revision/")
  suspend fun imageRevision(@Query("image") image: Int): Paginated<AstroImageRevision>

  @GET("api/v2/images/thumbnail-group/")
  suspend fun thumbnailGroup(@Query("image") imageId: Int): ThumbnailGroup

  @GET("api/v2/nestedcomments/nestedcomments/")
  suspend fun comments(
    @Query("content_type") contentType: Int,
    @Query("object_id") objectId: Int,
  ): List<AstroComment>

  @POST("api/v2/nestedcomments/")
  suspend fun createComment(
    @Body comment: AstroComment,
  ): AstroComment




  @GET("api/v2/platesolving/solutions/")
  suspend fun plateSolve(
    @Query("content_type") contentType: Int,
    @Query("object_id") objectId: Int
  ): List<PlateSolve>

  @GET("api/v2/platesolving/solutions/")
  suspend fun plateSolves(
    @Query("content_type") contentType: Int,
    @Query("object_ids") objectIds: String
  ): List<PlateSolve>

  @GET("api/v2/common/users/{id}/")
  suspend fun user(
    @Path("id") id: Int
  ): AstroUser

  @GET("api/v1/userprofile/{id}/")
  suspend fun userProfile(@Path("id") id: Int): AstroUserProfile

  @GET("api/v1/userprofile/")
  suspend fun userProfile(@Query("username") username: String): ListResponse<AstroUserProfile>

  @GET("api/v1/image/{hash}/")
  suspend fun imageOld(@Path("hash") hash: String): AstroImage

  @GET("api/v1/image/")
  suspend fun imageSearch(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int,
    @QueryMap params: Map<String, String>
  ): ListResponse<AstroImage>

  @GET("api/v1/toppick/")
  suspend fun topPicks(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int,
  ): ListResponse<TopPick>

  @GET("api/v1/toppicknominations/")
  suspend fun topPickNominations(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int,
  ): ListResponse<TopPick>

  @GET("api/v1/imageoftheday/")
  suspend fun imageOfTheDay(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int,
  ): ListResponse<TopPick>
}

