package com.example.astrobin.api

import android.app.Application
import com.example.astrobin.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {

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

  @Provides
  fun baseOkHttpClient(application: Application) = OkHttpClient
    .Builder()
    .addInterceptor(authQueryAppenderInterceptor)
    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    .cache(
      Cache(
        directory = File(application.cacheDir, "http_cache"),
        maxSize = 50L * 1024L * 1024L // 50 MiB
      )
    )
    .build()

  @Provides
  fun retrofit(baseOkHttpClient: OkHttpClient) = Retrofit
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

  @Provides
  fun astrobinApi(retrofit: Retrofit) = retrofit.create(AstrobinApi::class.java)
}