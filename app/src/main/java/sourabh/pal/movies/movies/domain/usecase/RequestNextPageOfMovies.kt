package sourabh.pal.movies.movies.domain.usecase

import sourabh.pal.movies.common.domain.NoMoreMoviesException
import sourabh.pal.movies.common.domain.pagination.Pagination
import sourabh.pal.movies.common.domain.repositories.MandiRepository
import javax.inject.Inject

class RequestNextPageOfMovies @Inject constructor(private val animalRepository: MandiRepository) {
  suspend operator fun invoke(
      searchQuery: String,
      pageToLoad: Int,
      pageSize: Int = Pagination.DEFAULT_PAGE_SIZE
  ): Pagination {
    val (movies, pagination) = animalRepository.searchMoviesRemotely(pageToLoad, searchQuery, pageSize)

    if (movies.isEmpty()) {
      throw NoMoreMoviesException("No movies :(")
    }

    animalRepository.storeMovies(movies)

    return pagination
  }
}