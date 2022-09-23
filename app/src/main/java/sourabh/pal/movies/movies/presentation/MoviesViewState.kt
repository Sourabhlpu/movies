package sourabh.pal.movies.movies.presentation

import sourabh.pal.movies.common.presentation.Event
import sourabh.pal.movies.common.presentation.model.UIMovie

data class MoviesViewState(
    val loading: Boolean = true,
    val movies: List<UIMovie> = emptyList(),
    val noMoreMoviesNearby: Boolean = false,
    val failure: Event<Throwable>? = null
)