package sourabh.pal.movies.common.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class CachedMovie(
    @PrimaryKey(autoGenerate = false)
    val movieId: String,
    val title: String,
    val poster: String,
    val year: String,
)
