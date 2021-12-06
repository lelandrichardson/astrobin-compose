package com.example.astrobin.api

import com.example.astrobin.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val authQueryAppenderInterceptor: Interceptor = Interceptor { chain ->
  val requestBuilder = chain.request().newBuilder()

  val url = chain.request().url
  val urlBuilder = url.newBuilder()
  if (url.queryParameter("api_key") == null) {
    urlBuilder.addQueryParameter("api_key", BuildConfig.ASTROBIN_API_KEY)
  }
  if (url.queryParameter("api_secret") == null) {
    urlBuilder.addQueryParameter("api_secret", BuildConfig.ASTROBIN_API_SECRET)
  }
  chain.proceed(
    requestBuilder
      .url(urlBuilder.build())
      .build()
  )
}

internal val baseOkHttpClient: OkHttpClient = OkHttpClient
  .Builder()
  .addInterceptor(authQueryAppenderInterceptor)
  .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
  .build()

internal val retrofit: Retrofit = Retrofit
  .Builder()
  .baseUrl("https://astrobin.com/api/v1/")
  .client(baseOkHttpClient)
  .addConverterFactory(
    MoshiConverterFactory.create(
      Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    )
  )
  .build()