package sourabh.pal.movies.common.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import sourabh.pal.movies.common.domain.model.Movie

@Entity(tableName = "movies")
data class CachedMovie(
    @PrimaryKey(autoGenerate = false)
    val movieId: String,
    val title: String,
    val poster: String,
    val year: String,
){
    companion object{
        fun fromDomain(movie: Movie): CachedMovie{
            return CachedMovie(
                movieId = movie.id,
                title = movie.title,
                poster = movie.poster,
                year = movie.year
            )
        }

    }

    fun toDomain(): Movie{
        return Movie(
            id = movieId,
            title = title,
            poster = poster,
            year = year
        )
    }
}
