package sourabh.pal.movies.common.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {
  fun io(): CoroutineDispatcher = Dispatchers.IO
  fun main(): CoroutineDispatcher = Dispatchers.Main
}