package sourabh.pal.movies.sell.domain.usecase

import sourabh.pal.movies.common.domain.model.Village
import sourabh.pal.movies.common.domain.repositories.MandiRepository

import javax.inject.Inject

class GetVillagesUseCase @Inject constructor(private val repository: MandiRepository) {

    suspend operator fun invoke(): List<Village>{
       return repository.getVillages()
    }
}