package com.example.astrobin.api

import android.app.Application
import coil.ImageLoader
import coil.util.CoilUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {
  @Provides
  fun imageLoader(
    application: Application,
    okHttpClient: OkHttpClient
  ) = ImageLoader.Builder(application)
    .okHttpClient(
      okHttpClient.newBuilder()
        .cache(CoilUtils.createDefaultCache(application))
        .build()
    )
    .build()
}