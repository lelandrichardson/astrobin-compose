package com.example.astrobin.api

import androidx.paging.PagingSource
import androidx.paging.PagingState


abstract class AstroPagingSource<T : Any> : PagingSource<Int, T>() {
  abstract suspend fun load(limit: Int, offset: Int): ListResponse<T>
  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
    return try {
      val offset = params.key ?: 0
      val response = load(limit = params.loadSize, offset = offset)
      val total = response.meta.total_count
      val prevOffset = offset - params.loadSize
      val nextOffset = offset + params.loadSize
      LoadResult.Page(
        data = response.objects,
        prevKey = if (prevOffset < 0) null else prevOffset,
        nextKey = if (nextOffset > total) null else nextOffset
      )
    } catch (e: Exception) {
      LoadResult.Error(e)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, T>): Int? {
    return null
  }
}

class TopPickPagingSource(
  private val api: Astrobin
) : AstroPagingSource<TopPick>() {
  override suspend fun load(limit: Int, offset: Int): ListResponse<TopPick> {
    return api.topPicks(limit, offset)
  }
}

class ImageOfTheDayPagingSource(
  private val api: Astrobin
) : AstroPagingSource<TopPick>() {
  override suspend fun load(limit: Int, offset: Int): ListResponse<TopPick> {
    return api.imageOfTheDay(limit, offset)
  }
}

class ImageSearchPagingSource(
  private val api: Astrobin,
  val params: Map<String, String>
) : AstroPagingSource<AstroImage>() {
  override suspend fun load(limit: Int, offset: Int): ListResponse<AstroImage> {
    return api.imageSearch(limit, offset, params)
  }
}



