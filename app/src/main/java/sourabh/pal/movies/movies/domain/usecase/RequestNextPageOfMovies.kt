package sourabh.pal.movies.movies.domain.usecase

import sourabh.pal.movies.common.domain.repositories.MandiRepository
import javax.inject.Inject

class RequestNextPageOfMovies @Inject constructor(private val animalRepository: MandiRepository) {
  suspend operator fun invoke(
      pageToLoad: Int,
      pageSize: Int = Pagination.DEFAULT_PAGE_SIZE
  ): Pagination {
    val (animals, pagination) = animalRepository.requestMoreAnimals(pageToLoad, pageSize)

    if (animals.isEmpty()) {
      throw NoMoreAnimalsException("No animals nearby :(")
    }

    animalRepository.storeAnimals(animals)

    return pagination
  }
}