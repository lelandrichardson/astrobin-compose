package com.example.astrobin.api

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {
  @Provides
  fun imageLoader(
    application: Application,
    okHttpClient: OkHttpClient,
    @ApplicationContext context: Context
  ) = ImageLoader.Builder(application)
    .okHttpClient(
      okHttpClient.newBuilder()
         .build()
    ).memoryCache {
      MemoryCache.Builder(context)
        .maxSizePercent(0.25)
        .build()
    }
    .diskCache {
      DiskCache.Builder()
        .directory(context.cacheDir.resolve("image_cache"))
        .maxSizePercent(0.02)
        .build()
    }
    .build()
}
