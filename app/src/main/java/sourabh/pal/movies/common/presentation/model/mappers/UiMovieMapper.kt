package sourabh.pal.movies.common.presentation.model.mappers

import sourabh.pal.movies.common.domain.model.Movie
import sourabh.pal.movies.common.presentation.model.UIMovie
import javax.inject.Inject

class UiMovieMapper @Inject constructor(): UiMapper<Movie, UIMovie> {
    override fun mapToView(input: Movie): UIMovie {
        return UIMovie("Lord of the Rings")
    }
}