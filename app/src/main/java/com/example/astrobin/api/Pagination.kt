package com.example.astrobin.api

import com.squareup.moshi.Json


data class AstroResultsMeta(
  @field:Json(name="limit") val limit: Int,
  @field:Json(name="next") val next: String?,
  @field:Json(name="offset") val offset: Int,
  @field:Json(name="previous") val previous: String?,
  @field:Json(name="total_count") val total_count: Int,
)

data class ListResponse<T>(
  @field:Json(name="meta") val meta: AstroResultsMeta,
  @field:Json(name="objects") val objects: List<T>,
)

data class Paginated<T>(
  @field:Json(name="count") val count: Int,
  @field:Json(name="next") val next: String?,
  @field:Json(name="prev") val prev: String?,
  @field:Json(name="results") val results: List<T>,
)
