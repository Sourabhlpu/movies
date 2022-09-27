package sourabh.pal.movies.movies.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import sourabh.pal.movies.common.domain.model.Movie
import sourabh.pal.movies.common.domain.pagination.Pagination
import sourabh.pal.movies.common.domain.repositories.MandiRepository
import javax.inject.Inject


class SearchMoviesLocally @Inject constructor(private val animalRepository: MandiRepository) {
    operator fun invoke(
        searchFlow: StateFlow<String>
    ): Flow<List<Movie>> {
        return searchFlow
            .debounce(300)
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .flatMapLatest { animalRepository.searchCachedMoviesBy(it) }
            .flowOn(Dispatchers.IO)
    }
}