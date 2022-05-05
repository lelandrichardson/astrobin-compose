package com.example.astrobin.exp

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun <R> StateFlow<R>.collectAsState(
  context: CoroutineContext = EmptyCoroutineContext
): State<R> =  produceState(this.value, this, context) {
  if (context == EmptyCoroutineContext) {
    collect { value = it }
  } else withContext(context) {
    collect { value = it }
  }
}

@Composable
fun <T> loadFlow(
  initial: T,
  build: suspend ProducerScope<(T) -> T>.() -> Unit
): Loadable<T> {
  val scope = rememberCoroutineScope()
  val flow = remember {
    val start = Loadable(initial, true)
    val last: (T) -> T = { it }
    channelFlow {
      build()
      send(last)
    }
      .runningFold(start) { acc, it ->
          acc.copy(value = it(acc.value), it !== last)
      }
      .stateIn(scope, SharingStarted.Eagerly, start)
  }
  return flow.collectAsState().value
}
