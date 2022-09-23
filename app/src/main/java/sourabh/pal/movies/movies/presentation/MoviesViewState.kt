package sourabh.pal.movies.movies.presentation

import sourabh.pal.movies.common.presentation.Event
import sourabh.pal.movies.common.presentation.model.UIMovie

data class MoviesViewState(
    val loading: Boolean = true,
    val animals: List<UIMovie> = emptyList(),
    val noMoreAnimalsNearby: Boolean = false,
    val failure: Event<Throwable>? = null
)