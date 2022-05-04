package com.example.astrobin.api

import android.app.Application
import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
object AstrobinComponent {

  @Provides
  fun authenticationInterceptor(
    application: Application,
    authApi: AstrobinAuthApi
  ): AuthenticationInterceptor = AuthenticationInterceptor(
    authApi,
    application.getSharedPreferences("com.example.astrobin", Context.MODE_PRIVATE)
  )

  @Provides
  fun baseOkHttpClient(
    application: Application,
    authenticationInterceptor: AuthenticationInterceptor
  ) = OkHttpClient
    .Builder()
    .addInterceptor {
      it.proceed(
        it
          .request()
          .newBuilder()
          .header("Accept","application/json")
          .build()
      )
    }
    .addInterceptor(authenticationInterceptor)
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
    .baseUrl("https://www.astrobin.com/")
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
  fun astrobinApi(retrofit: Retrofit): AstrobinApi {
    return retrofit
      .create(AstrobinApi::class.java)
  }

  @Provides
  fun authApi(): AstrobinAuthApi {
    return Retrofit
      .Builder()
      .baseUrl("https://www.astrobin.com/")
      .client(OkHttpClient
        .Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()
      )
      .addConverterFactory(
        MoshiConverterFactory.create(
          Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        )
      )
      .build()
      .create(AstrobinAuthApi::class.java)
  }

  @Provides
  fun astrobin(
    authenticationInterceptor: AuthenticationInterceptor,
    astrobinApi: AstrobinApi,
  ) = Astrobin(authenticationInterceptor, astrobinApi)
}