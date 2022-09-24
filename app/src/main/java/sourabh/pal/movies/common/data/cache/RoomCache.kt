package sourabh.pal.movies.common.data.cache

import kotlinx.coroutines.flow.Flow
import sourabh.pal.movies.common.data.cache.daos.MovieDao
import sourabh.pal.movies.common.data.cache.model.CachedMovie
import javax.inject.Inject

class RoomCache @Inject constructor(
private val moviesDao: MovieDao
): Cache{
    override suspend fun storeMovies(movies: List<CachedMovie>) {
        moviesDao.insertMovies(movies)
    }

    override fun searchMovies(name: String): Flow<List<CachedMovie>> {
        return moviesDao.searchMovieByName(name)
    }
}