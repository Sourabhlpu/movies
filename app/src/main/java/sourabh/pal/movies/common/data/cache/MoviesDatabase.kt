package sourabh.pal.movies.common.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import sourabh.pal.movies.common.data.cache.daos.MovieDao
import sourabh.pal.movies.common.data.cache.model.CachedMovie

@Database(
    entities = [CachedMovie::class],
    version = 1
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MovieDao
}