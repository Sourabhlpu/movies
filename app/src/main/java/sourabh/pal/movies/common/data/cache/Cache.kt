package sourabh.pal.movies.common.data.cache

import kotlinx.coroutines.flow.Flow
import sourabh.pal.movies.common.data.cache.model.CachedMovie

interface Cache {
  suspend fun storeMovies(movies: List<CachedMovie>)
  fun searchMovies(name: String): Flow<List<CachedMovie>>
  fun getAllMovies(): Flow<List<CachedMovie>>
}