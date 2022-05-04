package com.example.astrobin.api

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.astrobin.BuildConfig
import com.squareup.moshi.Json
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


private const val TOKEN_KEY: String = "astrobin.api.access_token"

interface AstrobinAuthApi {
  @FormUrlEncoded
  @POST("api/v2/api-auth-token/")
  suspend fun authToken(
    @Field("username") username: String,
    @Field("password") password: String
  ): ApiToken
}

data class ApiToken(
  @field:Json(name="token") val token: String
)

class AuthenticationInterceptor(
  private val auth: AstrobinAuthApi,
  private val sharedPrefs: SharedPreferences
) : Interceptor {
  suspend fun setCredentials(username: String, password: String): Boolean {
    clear()
    try {
      val token = auth.authToken(username, password)
      sharedPrefs.edit {
        putString(TOKEN_KEY, token.token)
      }
    } catch (e: Exception) {
      return false
    }
    return true
  }

  fun clear() {
    sharedPrefs.edit {
      remove(TOKEN_KEY)
    }
  }

  fun isLoggedIn(): Boolean = token() != null

  private fun token(): String? {
    return sharedPrefs.getString(TOKEN_KEY, null)
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val url = request.url
    if (url.encodedPath.startsWith("/api/v2/")) {
      Log.d("LELAND", "path started with apiv2!")
      val token = token()
      if (token != null) {
        Log.d("LELAND", "token was non null!")
        return chain.proceed(
          request
            .newBuilder()
            .header("Authorization", "Token $token")
            .build()
        )
      }
      // authorization header will be added by the authenticator
      return chain.proceed(request)
    } else if (url.encodedPath.startsWith("/api/v1/")) {
      // even if we are authenticated, we want to add the api key/secret to the url here
      val requestBuilder = request.newBuilder()
      val urlBuilder = url.newBuilder()
      if (url.queryParameter("api_key") == null) {
        urlBuilder.addQueryParameter("api_key", BuildConfig.ASTROBIN_API_KEY)
      }
      if (url.queryParameter("api_secret") == null) {
        urlBuilder.addQueryParameter("api_secret", BuildConfig.ASTROBIN_API_SECRET)
      }
      return chain.proceed(
        requestBuilder
          .url(urlBuilder.build())
          .build()
      )
    } else {
      return chain.proceed(request)
    }
  }
}
