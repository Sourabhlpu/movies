

package sourabh.pal.movies.movies.presentation

sealed class MoviesEvent {
  object RequestInitialMovieList: MoviesEvent()
  object RequestMoreMovies: MoviesEvent()
  data class QueryInput(val query: String): MoviesEvent()
}