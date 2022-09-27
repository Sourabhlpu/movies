package sourabh.pal.movies.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sourabh.pal.movies.common.domain.NetworkException
import sourabh.pal.movies.common.domain.NetworkUnavailableException
import sourabh.pal.movies.common.domain.NoMoreMoviesException
import sourabh.pal.movies.common.domain.model.Movie
import sourabh.pal.movies.common.domain.pagination.Pagination
import sourabh.pal.movies.common.presentation.Event
import sourabh.pal.movies.common.presentation.model.mappers.UiMovieMapper
import sourabh.pal.movies.common.utils.DispatchersProvider
import sourabh.pal.movies.common.utils.createExceptionHandler
import sourabh.pal.movies.movies.domain.usecase.RequestNextPageOfMovies
import sourabh.pal.movies.movies.domain.usecase.SearchMoviesLocally
import javax.inject.Inject


@HiltViewModel
class MoviesFragmentViewModel @Inject constructor(
    private val searchMoviesLocally: SearchMoviesLocally,
    private val requestNextPageOfMovies: RequestNextPageOfMovies,
    private val uiMovieMapper: UiMovieMapper,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    companion object {
        const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
    }

    val state: StateFlow<MoviesViewState> get() = _state
    var isLoadingMoreMovies: Boolean = false
    var isLastPage = false

    private val _state = MutableStateFlow(MoviesViewState())
    private var currentPage = 0

    private val _queryStateFlow: MutableStateFlow<String> = MutableStateFlow("")

    init {
        subscribeToMovieUpdates()
    }

    fun onEvent(event: MoviesEvent) {
        when (event) {
            is MoviesEvent.RequestInitialMovieList -> loadMovies()
            is MoviesEvent.RequestMoreMovies -> loadNextMoviePage()
            is MoviesEvent.QueryInput -> updateQuery(event.query)
        }
    }

    private fun updateQuery(query: String) {
        resetPagination()
        if(query.isEmpty())
            updateToEmptyQuery()
        else{
            _queryStateFlow.value = query
        }
    }

    private fun updateToEmptyQuery() {
        _state.value = state.value.copy(searchingRemotely = false, movies = emptyList(), noSearchQuery = true)
    }

    private fun subscribeToMovieUpdates() {
        viewModelScope.launch {
            searchMoviesLocally(_queryStateFlow)
                .catch { exception -> onFailure(exception) }
                .collect { movies ->
                    if(movies.isEmpty())
                        loadNextMoviePage()
                    else
                    onNewMoviesList(movies)
                }
        }
    }

    private fun onNewMoviesList(movies: List<Movie>) {
        val moviesUI = movies.map { uiMovieMapper.mapToView(it) }
        val currentList = state.value.movies
        val newMovies = moviesUI.subtract(currentList.toSet())
        val updatedList = currentList + newMovies

        _state.value = state.value.copy(searchingRemotely = false, movies = updatedList, noSearchQuery = false)
    }

    private fun loadMovies() {
        if (state.value.movies.isEmpty() && _queryStateFlow.value.isNotEmpty()) {
            loadNextMoviePage()
        }
    }

    private fun loadNextMoviePage() {
        isLoadingMoreMovies = true

        val errorMessage = "Failed to fetch movies"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

        viewModelScope.launch(exceptionHandler) {
            val pagination = withContext(dispatchersProvider.io()) {
                requestNextPageOfMovies(searchQuery = _queryStateFlow.value, ++currentPage)
            }
            onPaginationInfoObtained(pagination)
            isLoadingMoreMovies = false
        }
    }

    private fun onPaginationInfoObtained(pagination: Pagination) {
        currentPage = pagination.currentPage
        isLastPage = !pagination.canLoadMore
    }

    private fun resetPagination() {
        currentPage = 0
        _state.value = state.value.copy(
            movies = emptyList(),
            searchingRemotely = true
        )
    }

    private fun onFailure(failure: Throwable) {
        when (failure) {
            is NetworkException,
            is NetworkUnavailableException -> {
                _state.value = state.value.copy(
                    searchingRemotely = false,
                    failure = Event(failure)
                )
            }
            is NoMoreMoviesException -> {
                _state.value = state.value.copy(
                    searchingRemotely = false,
                    noMoreMovies = true,
                    failure = Event(failure)
                )
            }
        }
    }

}

