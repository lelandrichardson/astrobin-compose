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

abstract class AstroPaginatedPagingSource<T : Any> : PagingSource<Int, T>() {
  abstract suspend fun load(page: Int?): Paginated<T>
  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
    return try {
      val page = params.key ?: 1
      val response = load(page = page)
      LoadResult.Page(
        data = response.results,
        prevKey = if (response.prev != null) page - 1 else null,
        nextKey = if (response.next != null) page + 1 else null
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

class TopPickV2PagingSource(
  private val api: Astrobin
) : AstroPaginatedPagingSource<TopPickV2>() {
  override suspend fun load(page: Int?): Paginated<TopPickV2> {
    return api.topPicksV2(page ?: 1)
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



