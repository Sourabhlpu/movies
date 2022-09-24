package sourabh.pal.movies.common.data.cache.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sourabh.pal.movies.common.data.cache.model.CachedMovie

@Dao
abstract class MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovies(movies: List<CachedMovie>)

    @Transaction
    @Query("""SELECT * FROM movies WHERE title LIKE '%' || :name || '%'""")
    abstract fun searchMovieByName(name: String): Flow<List<CachedMovie>>

    @Query("SELECT * FROM movies")
    abstract fun getAllMovies(): Flow<List<CachedMovie>>
}