package com.example.astrobin.exp

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext


data class Loadable<T>(
  val value: T,
  val loading: Boolean,
)

class LoadableScope<T>(
  @PublishedApi
  internal val state: MutableState<Loadable<T>>,
  private val scope: CoroutineScope
) : CoroutineScope {
  inline fun push(fn: (T) -> T) {
    state.value = state.value.let { Loadable(fn(it.value), it.loading) }
  }

  override val coroutineContext: CoroutineContext
    get() = scope.coroutineContext
}

@Composable
fun <T> load(initial: T, build: suspend LoadableScope<T>.() -> Unit): Loadable<T> {
  val result = remember { mutableStateOf(Loadable(initial, true)) }
  LaunchedEffect(Unit) {
    coroutineScope {
      LoadableScope(result, this).build()
    }
    result.value = Loadable(result.value.value, false)
  }
  return result.value
}
