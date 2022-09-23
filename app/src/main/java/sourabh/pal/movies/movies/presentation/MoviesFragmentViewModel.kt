package sourabh.pal.movies.movies.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import sourabh.pal.movies.movies.domain.usecase.GetMovies
import sourabh.pal.movies.movies.domain.usecase.RequestNextPageOfMovies
import javax.inject.Inject


@HiltViewModel
class MoviesFragmentViewModel @Inject constructor(
    private val getMovies: GetMovies,
    private val requestNextPageOfMovies: RequestNextPageOfMovies,
    private val uiMovieMapper: UiMovieMapper,
    private val dispatchersProvider: DispatchersProvider
): ViewModel() {

    companion object {
        const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
    }

    val state: LiveData<MoviesViewState> get() = _state
    var isLoadingMoreMovies: Boolean = false
    var isLastPage = false

    private val _state = MutableLiveData<MoviesViewState>()
    private var currentPage = 0

    init {
        _state.value = MoviesViewState()
        subscribeToAnimalUpdates()
    }

    fun onEvent(event: MoviesEvent) {
        when(event) {
            is MoviesEvent.RequestInitialMovieList -> loadMovies()
            is MoviesEvent.RequestMoreMovies -> loadNextMoviePage()
        }
    }

    private fun subscribeToAnimalUpdates() {
/*        getMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onNewAnimalList(it) },
                { onFailure(it) }
            )
            .addTo(compositeDisposable)*/
    }

    private fun onNewAnimalList(movies: List<Movie>) {

        val moviesUI = movies.map { uiMovieMapper.mapToView(it) }

        // This ensures that new items are added below the already existing ones, thus avoiding
        // repositioning of items that are already visible, as it can provide for a confusing UX. A
        // nice alternative to this would be to add an "updatedAt" field to the Room entities, so
        // that we could actually order them by something that we completely control.
        val currentList = state.value!!.movies
        val newMovies = moviesUI.subtract(currentList.toSet())
        val updatedList = currentList + newMovies

        _state.value = state.value!!.copy( loading = false, movies = updatedList)
    }

    private fun loadMovies() {
        if (state.value!!.movies.isEmpty()) {
            loadNextMoviePage()
        }
    }

    private fun loadNextMoviePage() {
        isLoadingMoreMovies = true

        val errorMessage = "Failed to fetch nearby animals"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

        viewModelScope.launch(exceptionHandler) {
            val pagination = withContext(dispatchersProvider.io()) {
                requestNextPageOfMovies(++currentPage)
            }
            onPaginationInfoObtained(pagination)
            isLoadingMoreMovies = false
        }
    }

    private fun onPaginationInfoObtained(pagination: Pagination) {
        currentPage = pagination.currentPage
        isLastPage = !pagination.canLoadMore
    }

    private fun onFailure(failure: Throwable) {
        when (failure) {
            is NetworkException,
            is NetworkUnavailableException -> {
                _state.value = state.value!!.copy(
                    loading = false,
                    failure = Event(failure)
                )
            }
            is NoMoreMoviesException -> {
                _state.value = state.value!!.copy(
                    noMoreMoviesNearby = true,
                    failure = Event(failure)
                )
            }
        }
    }

}

