package sourabh.pal.movies.movies.presentation

import sourabh.pal.movies.common.presentation.Event
import sourabh.pal.movies.common.presentation.model.UIMovie

data class MoviesViewState(
    val noSearchQuery: Boolean = true,
    val movies: List<UIMovie> = emptyList(),
    val searchingRemotely: Boolean = false,
    val noRemoteResults: Boolean = false,
    val noMoreMovies: Boolean = false,
    val failure: Event<Throwable>? = null
)