package sourabh.pal.movies.common.domain.pagination

import sourabh.pal.movies.common.domain.model.Movie

data class PaginatedMovies(
    val animals: List<Movie>,
    val pagination: Pagination
)