package sourabh.pal.movies.movies.domain.usecase

import sourabh.pal.movies.common.domain.repositories.MandiRepository
import javax.inject.Inject

class GetMovies @Inject constructor(private val animalRepository: MandiRepository) {
  operator fun invoke() = animalRepository.getVillages()
      .filter { it.isNotEmpty() }
}